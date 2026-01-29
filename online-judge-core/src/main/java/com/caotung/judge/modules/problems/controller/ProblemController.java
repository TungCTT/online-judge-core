package com.caotung.judge.modules.problems.controller;


import com.caotung.judge.modules.problems.dto.ProblemCreateDTO;
import com.caotung.judge.modules.problems.entity.Problem;
import com.caotung.judge.modules.problems.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping
    public Problem create(@RequestBody ProblemCreateDTO dto){
        return problemService.createProblem(dto);
    }

    @GetMapping
    public List<Problem> list(){
        return problemService.getAllProblems();
    }
}
