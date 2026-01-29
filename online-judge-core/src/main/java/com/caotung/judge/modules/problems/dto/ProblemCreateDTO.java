package com.caotung.judge.modules.problems.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProblemCreateDTO {
    private String title;
    private String description;
    private String slug;
    private Integer timeLimitMs;
    private Integer memoryLimitMb;
    private List<TestCaseDTO> testCases;

    @Data
    public static class TestCaseDTO {
        private String input;
        private String output;
        private boolean isHidden;
    }
}