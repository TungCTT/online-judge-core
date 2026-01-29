package com.caotung.judge.modules.problems.service;

import com.caotung.judge.modules.problems.dto.ProblemCreateDTO;
import com.caotung.judge.modules.problems.entity.Problem;
import com.caotung.judge.modules.problems.entity.TestCase;
import com.caotung.judge.modules.problems.repository.ProblemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    @Transactional
    public Problem createProblem(ProblemCreateDTO dto){
        Problem problem = Problem.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .slug(dto.getSlug())
                .timeLimitMs(dto.getTimeLimitMs())
                .memoryLimitMb(dto.getMemoryLimitMb())
                .build();
        List<TestCase> testCases = dto.getTestCases().stream().map(tc ->
                TestCase.builder()
                        .inputData(tc.getInput())
                        .expectedOutput(tc.getOutput())
                        .isHidden(tc.isHidden())
                        .problem(problem)
                        .build()).toList();
        problem.setTestCases(testCases);
        return problemRepository.save(problem);
    }

    public List<Problem> getAllProblems(){
        return problemRepository.findAll();
    }
}
