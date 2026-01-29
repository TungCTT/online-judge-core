package com.caotung.judge.modules.submissions.controller;

import com.caotung.judge.modules.submissions.dto.SubmissionRequest;
import com.caotung.judge.modules.submissions.entity.Submission;
import com.caotung.judge.modules.submissions.repository.SubmissionRepository;
import com.caotung.judge.modules.submissions.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;

    @PostMapping
    public Submission submit(@RequestBody SubmissionRequest request) {
        return submissionService.submitCode(request);
    }
    @GetMapping
    public List<Submission> getSubmissions() {
        return submissionRepository.findAll();
    }
}