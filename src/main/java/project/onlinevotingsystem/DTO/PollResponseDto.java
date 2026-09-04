package project.onlinevotingsystem.DTO;

import lombok.Data;
import project.onlinevotingsystem.models.PollStatus;

import java.util.List;

@Data
public class PollResponseDto {
    private Long id;
    private String pollName;
    private PollStatus status;
    private boolean anonymous;
    private String url;
    private List<CandidateResponseDto> candidates;
}
