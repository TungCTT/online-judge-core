package com.caotung.judge.modules.problems.entity;

import com.caotung.judge.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "problems")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Problem extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String slug; // Ví dụ: "tinh-tong-hai-so"

    @Column(columnDefinition = "TEXT")
    private String templateCode;

    private Integer timeLimitMs;   // VD: 1000 (ms)
    private Integer memoryLimitMb; // VD: 256 (MB)

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCases;
}