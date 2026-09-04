package project.onlinevotingsystem.service;

import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.CandidateDto;
import project.onlinevotingsystem.DTO.CandidateResponseDto;
import project.onlinevotingsystem.models.Candidate;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.repository.CandidateRepository;
import project.onlinevotingsystem.utils.CandidateMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public List<CandidateResponseDto> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(CandidateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CandidateResponseDto createCandidate(CandidateDto dto, User currentUser) {
        if (!candidateRepository.findByUserId(currentUser.getId()).isEmpty()) {
            throw new IllegalStateException("Користувач вже зареєстрований як кандидат");
        }

        Candidate candidate = Candidate.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .middleName(dto.getMiddleName())
                .user(currentUser)
                .build();

        return CandidateMapper.toDto(candidateRepository.save(candidate));
    }

    @Override
    public Candidate getById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Кандидат не знайдений"));
    }
}
