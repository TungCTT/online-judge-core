package com.caotung.judge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OnlineJudgeSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineJudgeSystemApplication.class, args);
	}

}
