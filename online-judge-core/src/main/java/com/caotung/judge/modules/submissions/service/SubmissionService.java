package com.caotung.judge.modules.submissions.service;

import com.caotung.judge.modules.problems.repository.ProblemRepository;
import com.caotung.judge.modules.submissions.dto.SubmissionRequest;
import com.caotung.judge.modules.submissions.entity.Submission;
import com.caotung.judge.modules.submissions.repository.SubmissionRepository;
import com.caotung.judge.modules.user.entity.User;
import com.caotung.judge.modules.user.repository.UserRepository;
import com.caotung.judge.modules.worker.services.JudgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final JudgeService judgeService;
    private final ProblemRepository problemRepository;

    public Submission submitCode(SubmissionRequest request) {
        // Lấy username từ SecurityContext (JWT)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Submission submission = Submission.builder()
                .problem(problemRepository.findById(request.getProblemId()).orElse(null))
                .user(user)
                .sourceCode(request.getSourceCode())
                .language(Submission.Language.valueOf(request.getLanguage()))
                .status(Submission.SubmissionStatus.PENDING)
                .build();


        Submission savedSubmission = submissionRepository.save(submission);

        judgeService.judge(savedSubmission);

        return savedSubmission;
    }
}