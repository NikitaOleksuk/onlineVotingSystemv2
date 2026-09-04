package project.onlinevotingsystem.service;

import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.DTO.PollResultDto;
import project.onlinevotingsystem.DTO.VoteDto;
import project.onlinevotingsystem.models.User;

public interface VoteService {
    PollResponseDto votePoll(Long pollId, VoteDto voteDto, User currentUser);
    PollResultDto getPollResults(Long pollId, User currentUser);
}
