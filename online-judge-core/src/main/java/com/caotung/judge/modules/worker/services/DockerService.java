package com.caotung.judge.modules.worker.services;

import com.caotung.judge.modules.worker.dto.RunResultDTO;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DockerService {
    private DockerClient getDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://localhost:2375")
                .build();
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        return DockerClientImpl.getInstance(config, httpClient);
    }

    public RunResultDTO runCode(String sourceCode, String inputData) {
        String submissionId = UUID.randomUUID().toString();
        String hostPath = "D:/temp_judge/" + submissionId;
        createTempFile(hostPath, "Main.java", sourceCode);
        createTempFile(hostPath, "input.txt", inputData);

        DockerClient docker = getDockerClient();
        String containerId = null;
        try {
            Volume containerVolume = new Volume("/app");
            Bind bind = new Bind(hostPath, containerVolume);
            HostConfig hostConfig = HostConfig.newHostConfig()
                    .withBinds(bind)
                    .withMemory(512 * 1024 * 1024L)
                    .withCpuCount(1L);

            String command = "javac Main.java && java Main < input.txt";
            CreateContainerResponse containerResponse = docker.createContainerCmd("eclipse-temurin:17-jdk-alpine")
                    .withWorkingDir("/app")
                    .withHostConfig(hostConfig)
                    .withCmd("sh", "-c", command)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();
            containerId = containerResponse.getId();
            long starttime = System.currentTimeMillis();

            docker.startContainerCmd(containerId).exec();

            StringBuilder outputBuffer = new StringBuilder();
            StringBuilder errorBuffer = new StringBuilder();

            try{
                docker.logContainerCmd(containerId)
                        .withStdOut(true)
                        .withStdErr(true)
                        .withFollowStream(true)
                        .exec(new ResultCallback.Adapter<Frame>(){
                            @Override
                            public void onNext(Frame item){
                                String log = new String(item.getPayload(), StandardCharsets.UTF_8);
                                switch (item.getStreamType()){
                                    case STDOUT -> outputBuffer.append(log);
                                    case STDERR -> errorBuffer.append(log);
                                    default -> {}
                                }
                            }
                        }).awaitCompletion(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                errorBuffer.append("TLE");
            }
            long endtime = System.currentTimeMillis();


            return RunResultDTO.builder()
                    .timeMs(endtime - starttime)
                    .stdOut(outputBuffer.toString())
                    .stdErr(errorBuffer.toString())
                    .build();
        } catch (Exception e) {
            log.error("Loi Docker", e);
            return RunResultDTO.builder().stdErr(e.getMessage())
                    .timeMs(0)
                    .memoryBytes(0)
                    .stdOut("").build();
        } finally {
            if (containerId != null) {
                try {
                    docker.removeContainerCmd(containerId).withForce(true).exec();
                } catch (Exception e) {
                }
            }
        }
    }
        private void createTempFile (String folderPath, String fileName, String content){
            try {
                Path path = Paths.get(folderPath);
                if (!Files.exists(path)) Files.createDirectories(path);
                Files.writeString(path.resolve(fileName), content, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Ko ghi dc file:" + e.getMessage());
            }
        }
    }