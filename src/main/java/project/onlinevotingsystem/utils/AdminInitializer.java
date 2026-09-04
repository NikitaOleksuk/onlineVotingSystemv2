package project.onlinevotingsystem.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.onlinevotingsystem.models.Role;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username:admin}")
    private String defaultAdminUsername;

    @Value("${app.default-admin.password:admin123}")
    private String defaultAdminPassword;

    @Value("${app.default-admin.email:admin@example.com}")
    private String defaultAdminEmail;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(defaultAdminUsername).isEmpty()) {
            User admin = User.builder()
                    .username(defaultAdminUsername)
                    .email(defaultAdminEmail)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Адміна створено: {} / {}", defaultAdminUsername, defaultAdminPassword);
        } else {
            log.info("Адмін {} вже існує", defaultAdminUsername);
        }
    }
}
