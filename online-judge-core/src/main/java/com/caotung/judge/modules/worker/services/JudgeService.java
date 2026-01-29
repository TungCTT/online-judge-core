package com.caotung.judge.modules.worker.services;

import com.caotung.judge.modules.problems.entity.Problem;
import com.caotung.judge.modules.problems.entity.TestCase;
import com.caotung.judge.modules.problems.repository.ProblemRepository;
import com.caotung.judge.modules.submissions.dto.SubmissionDetailDTO;
import com.caotung.judge.modules.submissions.entity.Submission;
import com.caotung.judge.modules.submissions.repository.SubmissionRepository;
import com.caotung.judge.modules.worker.dto.RunResultDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JudgeService {
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final DockerService dockerService;

    @Async
    @Transactional
    public void judge(Submission submission) {

        submission.setStatus(Submission.SubmissionStatus.JUDGING);
        submissionRepository.save(submission);

        try{
            Problem problem = problemRepository.findById(submission.getProblem().getId())
                    .orElseThrow(() -> new RuntimeException("Problem not found"));
            List<SubmissionDetailDTO> details = new ArrayList<>();
            boolean isAllCorrect = true;
            int maxTime = 0;
            int maxMemory = 0;

            for(TestCase t: problem.getTestCases()){
                RunResultDTO result = dockerService.runCode(submission.getSourceCode(), t.getInputData());
                String ans = result.getStdOut() == null?"":result.getStdOut().trim();
                String ans_expect = t.getExpectedOutput().trim();
                String status = "ACCEPTED";

                if(!ans.equals(ans_expect)){
                    status = "WA";
                    isAllCorrect = false;
                }
                if(result.getStdErr()!= null && !result.getStdErr().isEmpty()){
                    status = "RTE";
                    isAllCorrect = false;
                    submission.setErrorMessage(result.getStdErr());
                    log.error("Docker Err:{}", result.getStdErr());
                }

                details.add(
                        new SubmissionDetailDTO(
                                t.getId(),
                                status,
                                (int) result.getTimeMs(),
                                (int) result.getMemoryBytes() / 1024,
                                ans,
                                ans_expect
                        )
                );

                maxTime = Math.max(maxTime,(int) result.getTimeMs());
                maxMemory = Math.max(maxMemory,(int) result.getMemoryBytes() / 1024);
            }

            submission.setResultDetails(details);
            submission.setRunTimeMs(maxTime);
            submission.setMemoryUsageKb(maxMemory);
            submission.setStatus(isAllCorrect? Submission.SubmissionStatus.ACCEPTED : Submission.SubmissionStatus.WRONG_ANSWER);
            submission.setScore(isAllCorrect?100.0:0.0);
        } catch (Exception e){
            log.error(e.getMessage());
            submission.setStatus(Submission.SubmissionStatus.SYSTEM_ERROR);
            submission.setErrorMessage(e.getMessage());
        }

        submissionRepository.save(submission);
        log.info("Cham xong:{}. Ket qua:{}",submission.getId(),submission.getStatus());
    }
}
