package project.onlinevotingsystem.service;

import project.onlinevotingsystem.DTO.CandidateDto;
import project.onlinevotingsystem.DTO.CandidateResponseDto;
import project.onlinevotingsystem.models.Candidate;
import project.onlinevotingsystem.models.User;

import java.util.List;

public interface CandidateService {
    List<CandidateResponseDto> getAllCandidates();
    CandidateResponseDto createCandidate(CandidateDto dto, User currentUser);
    Candidate getById(Long id);
}
