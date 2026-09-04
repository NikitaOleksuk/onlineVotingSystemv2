package project.onlinevotingsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.RegisterRequest;
import project.onlinevotingsystem.models.Role;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Користувач не знайдений: " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createNewUser(RegisterRequest registerRequest) {
        validateUsername(registerRequest.getUsername());
        validateEmail(registerRequest.getEmail());
        validatePassword(registerRequest.getPassword(), registerRequest.getConfirmPassword());

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.USER);

        return save(user);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty() || username.length() < 3) {
            throw new IllegalArgumentException("Username повинен містити мінімум 3 символи.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username вже використовується.");
        }
    }

    private void validatePassword(String password, String confirmPassword) {
        if (password == null || password.trim().isEmpty() || password.length() < 8) {
            throw new IllegalArgumentException("Password повинен містити мінімум 8 символів.");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("Password повинен містити хоча б одну літеру та одну цифру.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Паролі не співпадають.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Невірний формат email.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email вже використовується.");
        }
    }
}
