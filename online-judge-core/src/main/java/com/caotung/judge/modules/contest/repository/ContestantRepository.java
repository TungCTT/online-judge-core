package com.caotung.judge.modules.contest.repository;

import com.caotung.judge.modules.contest.entity.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestantRepository extends JpaRepository<Contestant, Integer> {
}
