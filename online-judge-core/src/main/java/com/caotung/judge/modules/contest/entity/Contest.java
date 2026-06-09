package com.caotung.judge.modules.contest.entity;


import com.caotung.judge.common.entity.BaseEntity;
import com.caotung.judge.common.enums.ContestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "contests")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contest extends BaseEntity {
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private Integer duration;
    private ContestStatus status = ContestStatus.INACTIVE;
}
