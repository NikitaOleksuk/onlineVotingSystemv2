package project.onlinevotingsystem.utils;

import project.onlinevotingsystem.DTO.CandidateResponseDto;
import project.onlinevotingsystem.models.Candidate;

public class CandidateMapper {

    public static CandidateResponseDto toDto(Candidate candidate) {
        CandidateResponseDto dto = new CandidateResponseDto();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setSurname(candidate.getSurname());
        dto.setMiddleName(candidate.getMiddleName());
        if (candidate.getUser() != null) {
            dto.setUsername(candidate.getUser().getUsername());
        }
        return dto;
    }
}
