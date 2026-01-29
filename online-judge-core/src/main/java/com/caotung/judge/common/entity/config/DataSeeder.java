package com.caotung.judge.common.entity.config;

import com.caotung.judge.modules.problems.entity.Problem;
import com.caotung.judge.modules.problems.entity.TestCase;
import com.caotung.judge.modules.problems.repository.ProblemRepository;
import com.caotung.judge.modules.user.entity.User;
import com.caotung.judge.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_USERNAME:admin}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD:admin123}")
    private String adminPassword;

    @Value("${ADMIN_EMAIL:admin@test.com}")
    private String adminEmail;

    @Value("${TEST_USERNAME:student1}")
    private String testUsername;

    @Value("${TEST_PASSWORD:123456}")
    private String testPassword;

    @Value("${TEST_EMAIL:student1@test.com}")
    private String testEmail;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("Administrator")
                    .role(User.Role.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
            System.out.println("-----> Đã tạo Admin user: " + adminUsername + " / " + adminPassword);

            User user = User.builder()
                    .username(testUsername)
                    .password(passwordEncoder.encode(testPassword))
                    .email(testEmail)
                    .fullName("Student One")
                    .role(User.Role.MEMBER)
                    .isActive(true)
                    .build();
            userRepository.save(user);
            System.out.println("-----> Đã tạo Member user: " + testUsername + " / " + testPassword);
        }

        if (problemRepository.count() == 0) {
            Problem p = Problem.builder()
                    .title("Tính tổng hai số")
                    .description("Nhập vào 2 số nguyên a và b. In ra tổng của chúng.")
                    .slug("a-plus-b")
                    .timeLimitMs(1000)
                    .memoryLimitMb(256)
                    .build();

            TestCase tc1 = TestCase.builder().inputData("1 2").expectedOutput("3").problem(p).build();
            TestCase tc2 = TestCase.builder().inputData("10 20").expectedOutput("30").problem(p).build();

            p.setTestCases(List.of(tc1, tc2));

            problemRepository.save(p);
            System.out.println("-----> Đã tạo Problem mẫu: A + B");
        }
    }
}