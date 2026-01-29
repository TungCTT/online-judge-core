package com.caotung.judge.modules.submissions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
public class SubmissionDetailDTO implements Serializable {
    private UUID testCaseId;
    private String status;
    private Integer runTimeMs;
    private Integer memoryUsageKb;
    private String actualOutput;
    private String expectedOutput;
}