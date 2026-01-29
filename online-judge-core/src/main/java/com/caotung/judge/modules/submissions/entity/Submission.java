package com.caotung.judge.modules.submissions.entity;

import com.caotung.judge.common.entity.BaseEntity;
import com.caotung.judge.modules.problems.entity.Problem;
import com.caotung.judge.modules.submissions.dto.SubmissionDetailDTO;
import com.caotung.judge.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Submission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    private Double score;
    private Integer runTimeMs;
    private Integer memoryUsageKb;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<SubmissionDetailDTO> resultDetails = new ArrayList<>();


    public enum Language {
        JAVA, CPP, PYTHON, GO, JS
    }

    public enum SubmissionStatus {
        PENDING,
        JUDGING,
        ACCEPTED,
        WRONG_ANSWER,
        TIME_LIMIT_EXCEEDED,
        MEMORY_LIMIT_EXCEEDED,
        COMPILE_ERROR,
        SYSTEM_ERROR
    }
}