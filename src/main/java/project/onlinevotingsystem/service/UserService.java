package project.onlinevotingsystem.service;

import project.onlinevotingsystem.DTO.RegisterRequest;
import project.onlinevotingsystem.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);
    User createNewUser(RegisterRequest registerRequest);
    User save(User user);
    Optional<User> findById(Long id);
}
