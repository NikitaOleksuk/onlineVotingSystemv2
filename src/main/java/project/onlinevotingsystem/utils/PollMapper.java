package project.onlinevotingsystem.utils;

import project.onlinevotingsystem.DTO.CandidateResponseDto;
import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.models.Poll;

import java.util.List;
import java.util.stream.Collectors;

public class PollMapper {

    public static PollResponseDto toDto(Poll poll) {
        PollResponseDto dto = new PollResponseDto();
        dto.setId(poll.getId());
        dto.setPollName(poll.getPollName());
        dto.setStatus(poll.getStatus());
        dto.setAnonymous(poll.isAnonymous());
        dto.setUrl(poll.getUrl());

        List<CandidateResponseDto> candidateDtos = poll.getCandidates().stream()
                .map(CandidateMapper::toDto)
                .collect(Collectors.toList());
        dto.setCandidates(candidateDtos);

        return dto;
    }
}
