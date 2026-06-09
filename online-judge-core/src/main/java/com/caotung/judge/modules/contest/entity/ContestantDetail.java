package com.caotung.judge.modules.contest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ContestantDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long contestantId;
    private UUID problemId;
    private Boolean is_solve;
    private Integer failed_attempts;
    private Time time_taken;
}
