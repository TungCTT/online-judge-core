package com.caotung.judge.modules.submissions.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SubmissionRequest {
    private UUID problemId;
    private String sourceCode;
    private String language;
}
