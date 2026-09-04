package project.onlinevotingsystem.controller;

import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.LoginRequest;
import project.onlinevotingsystem.DTO.RegisterRequest;
import project.onlinevotingsystem.service.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody LoginRequest request) {
        return authService.createAuthToken(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody RegisterRequest request) {
        return authService.createNewUser(request);
    }
}
