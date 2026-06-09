package com.caotung.judge.modules.contest.repository;

import com.caotung.judge.modules.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContestRepository extends JpaRepository<Contest, UUID> {
}
