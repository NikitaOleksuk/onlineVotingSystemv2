package project.onlinevotingsystem.controller;

import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.PollDto;
import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.service.PollService;
import project.onlinevotingsystem.utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;
    private final AuthUtils authUtils;

    @GetMapping
    public List<PollResponseDto> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{pollId}")
    public PollResponseDto getPollForVoting(@PathVariable Long pollId) {
        return pollService.getPollForVoting(pollId);
    }

    @PostMapping
    public ResponseEntity<PollResponseDto> createPoll(@RequestBody PollDto dto) {
        User currentUser = authUtils.getCurrentUser();
        PollResponseDto created = pollService.createPoll(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{pollId}/start")
    public ResponseEntity<PollResponseDto> startPoll(@PathVariable Long pollId) {
        User currentUser = authUtils.getCurrentUser();
        return ResponseEntity.ok(pollService.startPoll(pollId, currentUser));
    }

    @PostMapping("/{pollId}/stop")
    public ResponseEntity<PollResponseDto> stopPoll(@PathVariable Long pollId) {
        User currentUser = authUtils.getCurrentUser();
        return ResponseEntity.ok(pollService.stopPoll(pollId, currentUser));
    }

    @PostMapping("/{pollId}/pause")
    public ResponseEntity<PollResponseDto> pausePoll(@PathVariable Long pollId) {
        User currentUser = authUtils.getCurrentUser();
        return ResponseEntity.ok(pollService.pausePoll(pollId, currentUser));
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long pollId) {
        User currentUser = authUtils.getCurrentUser();
        pollService.deletePoll(pollId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
