package project.onlinevotingsystem.DTO;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PollResultDto {
    private String pollName;
    private Map<String, Long> results;
    // Only populated for non-anonymous polls
    private Map<String, List<VoterDto>> votersByCandidate;
}
