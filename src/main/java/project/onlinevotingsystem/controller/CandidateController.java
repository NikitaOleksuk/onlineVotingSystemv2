package project.onlinevotingsystem.controller;

import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.CandidateDto;
import project.onlinevotingsystem.DTO.CandidateResponseDto;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.service.CandidateService;
import project.onlinevotingsystem.utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final AuthUtils authUtils;

    @GetMapping
    public List<CandidateResponseDto> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @PostMapping
    public ResponseEntity<CandidateResponseDto> createCandidate(@RequestBody CandidateDto dto) {
        User currentUser = authUtils.getCurrentUser();
        CandidateResponseDto created = candidateService.createCandidate(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
