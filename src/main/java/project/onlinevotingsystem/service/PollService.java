package project.onlinevotingsystem.service;

import project.onlinevotingsystem.DTO.PollDto;
import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.models.User;

import java.util.List;

public interface PollService {
    List<PollResponseDto> getAllPolls();
    PollResponseDto createPoll(PollDto dto, User currentUser);
    PollResponseDto startPoll(Long pollId, User currentUser);
    PollResponseDto stopPoll(Long pollId, User currentUser);
    PollResponseDto pausePoll(Long pollId, User currentUser);
    void deletePoll(Long pollId, User currentUser);
    PollResponseDto getPollForVoting(Long pollId);
}
