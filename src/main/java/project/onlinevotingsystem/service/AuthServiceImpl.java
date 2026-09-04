package project.onlinevotingsystem.service;

import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.JwtResponse;
import project.onlinevotingsystem.DTO.LoginRequest;
import project.onlinevotingsystem.DTO.RegisterRequest;
import project.onlinevotingsystem.DTO.RegistrationDTO;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.utils.JwtTokenUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));

        String token = jwtTokenUtils.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(user.getId(), user.getUsername(), user.getEmail(), token));
    }

    public ResponseEntity<?> createNewUser(RegisterRequest request) {
        User user = userService.createNewUser(request);
        String token = jwtTokenUtils.generateToken(user);
        return ResponseEntity.ok(new RegistrationDTO(user.getId(), user.getUsername(), user.getEmail(), token));
    }
}
