package project.onlinevotingsystem.controller;

import lombok.RequiredArgsConstructor;
import project.onlinevotingsystem.DTO.PollResponseDto;
import project.onlinevotingsystem.DTO.PollResultDto;
import project.onlinevotingsystem.DTO.VoteDto;
import project.onlinevotingsystem.models.User;
import project.onlinevotingsystem.service.VoteService;
import project.onlinevotingsystem.utils.AuthUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final AuthUtils authUtils;

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<PollResponseDto> votePoll(
            @PathVariable Long pollId,
            @RequestBody VoteDto voteDto) {
        User currentUser = authUtils.getCurrentUser();
        return ResponseEntity.ok(voteService.votePoll(pollId, voteDto, currentUser));
    }

    @GetMapping("/{pollId}/results")
    public ResponseEntity<PollResultDto> getPollResults(@PathVariable Long pollId) {
        User currentUser = authUtils.getCurrentUser();
        return ResponseEntity.ok(voteService.getPollResults(pollId, currentUser));
    }
}
