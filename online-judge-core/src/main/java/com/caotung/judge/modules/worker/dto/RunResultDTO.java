package com.caotung.judge.modules.worker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunResultDTO {
    private String stdOut;
    private String stdErr;
    private long timeMs;
    private long memoryBytes;
    private boolean isTimeLimit;
}