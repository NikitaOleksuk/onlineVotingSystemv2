package project.onlinevotingsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.DTO.PollResultDto;
import project.onlinevotingsystem.DTO.VoteDto;
import project.onlinevotingsystem.DTO.VoterDto;
import project.onlinevotingsystem.models.Candidate;
import project.onlinevotingsystem.models.Poll;
import project.onlinevotingsystem.models.PollStatus;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.models.Vote;
import project.onlinevotingsystem.repository.CandidateRepository;
import project.onlinevotingsystem.repository.PollRepository;
import project.onlinevotingsystem.utils.PollMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final PollRepository pollRepository;
    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public PollResponseDto votePoll(Long pollId, VoteDto voteDto, User currentUser) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Опитування не знайдено"));

        if (poll.getStatus() == PollStatus.PAUSED) {
            throw new IllegalStateException("Опитування на паузі, голосування призупинено");
        }
        if (poll.getStatus() != PollStatus.RUNNING) {
            throw new IllegalStateException("Голосування можливе лише у активному опитуванні");
        }

        boolean alreadyVoted = poll.getVotes().stream()
                .anyMatch(vote -> vote.getUser().getId().equals(currentUser.getId()));
        if (alreadyVoted) {
            throw new IllegalStateException("Ви вже проголосували у цьому опитуванні");
        }

        Candidate candidate = candidateRepository.findById(voteDto.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Кандидат не знайдений"));

        if (!poll.getCandidates().contains(candidate)) {
            throw new IllegalStateException("Кандидат не бере участь у цьому опитуванні");
        }

        Vote vote = Vote.builder()
                .poll(poll)
                .candidate(candidate)
                .user(currentUser)
                .build();

        poll.getVotes().add(vote);
        return PollMapper.toDto(pollRepository.save(poll));
    }

    @Override
    public PollResultDto getPollResults(Long pollId, User currentUser) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Опитування не знайдено"));

        if (!poll.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("Доступ заборонено: лише власник може переглядати результати");
        }

        Map<String, Long> results = poll.getVotes().stream()
                .collect(Collectors.groupingBy(
                        vote -> getFullName(vote.getCandidate()),
                        Collectors.counting()
                ));

        PollResultDto resultDto = new PollResultDto();
        resultDto.setPollName(poll.getPollName());
        resultDto.setResults(results);

        if (!poll.isAnonymous()) {
            Map<String, List<VoterDto>> votersByCandidate = poll.getVotes().stream()
                    .collect(Collectors.groupingBy(
                            vote -> getFullName(vote.getCandidate()),
                            Collectors.mapping(vote -> {
                                VoterDto voterDto = new VoterDto();
                                voterDto.setUsername(vote.getUser().getUsername());
                                voterDto.setEmail(vote.getUser().getEmail());
                                return voterDto;
                            }, Collectors.toList())
                    ));
            resultDto.setVotersByCandidate(votersByCandidate);
        }

        return resultDto;
    }

    private String getFullName(Candidate candidate) {
        return candidate.getSurname() + " " + candidate.getName()
                + (candidate.getMiddleName() != null ? " " + candidate.getMiddleName() : "");
    }
}
