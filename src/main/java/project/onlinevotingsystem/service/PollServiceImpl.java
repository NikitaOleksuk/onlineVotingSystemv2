package project.onlinevotingsystem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.PollDto;
import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.models.Candidate;
import project.onlinevotingsystem.models.Poll;
import project.onlinevotingsystem.models.PollStatus;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.repository.CandidateRepository;
import project.onlinevotingsystem.repository.PollRepository;
import project.onlinevotingsystem.utils.PollMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final PollRepository pollRepository;
    private final CandidateRepository candidateRepository;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public List<PollResponseDto> getAllPolls() {
        return pollRepository.findAll().stream()
                .map(PollMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PollResponseDto createPoll(PollDto dto, User currentUser) {
        List<Candidate> candidates = dto.getCandidateIds() == null
                ? List.of()
                : dto.getCandidateIds().stream()
                    .map(id -> candidateRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Кандидат не знайдений: " + id)))
                    .collect(Collectors.toList());

        Poll poll = Poll.builder()
                .pollName(dto.getPollName())
                .status(PollStatus.CREATED)
                .anonymous(dto.isAnonymous())
                .createdBy(currentUser)
                .candidates(candidates)
                .build();

        Poll saved = pollRepository.save(poll);
        return PollMapper.toDto(saved);
    }

    @Override
    @Transactional
    public PollResponseDto startPoll(Long pollId, User currentUser) {
        Poll poll = getPollAndValidateOwnership(pollId, currentUser);
        poll.setStatus(PollStatus.RUNNING);

        String voteUrl = baseUrl + "/poll/" + poll.getId();
        poll.setUrl(voteUrl);

        return PollMapper.toDto(pollRepository.save(poll));
    }

    @Override
    @Transactional
    public PollResponseDto stopPoll(Long pollId, User currentUser) {
        Poll poll = getPollAndValidateOwnership(pollId, currentUser);
        poll.setStatus(PollStatus.STOPPED);
        return PollMapper.toDto(pollRepository.save(poll));
    }

    @Override
    @Transactional
    public PollResponseDto pausePoll(Long pollId, User currentUser) {
        Poll poll = getPollAndValidateOwnership(pollId, currentUser);
        poll.setStatus(PollStatus.PAUSED);
        return PollMapper.toDto(pollRepository.save(poll));
    }

    @Override
    @Transactional
    public void deletePoll(Long pollId, User currentUser) {
        Poll poll = getPollAndValidateOwnership(pollId, currentUser);
        if (poll.getStatus() == PollStatus.RUNNING) {
            throw new IllegalStateException("Неможливо видалити активне опитування!");
        }
        pollRepository.delete(poll);
    }

    @Override
    public PollResponseDto getPollForVoting(Long pollId) {
        return pollRepository.findById(pollId)
                .filter(poll -> poll.getStatus() == PollStatus.RUNNING)
                .map(PollMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Опитування не знайдено або воно не активне"));
    }

    private Poll getPollAndValidateOwnership(Long pollId, User currentUser) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Опитування не знайдено"));
        if (!poll.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("Доступ заборонено: ви не є власником опитування");
        }
        return poll;
    }
}
