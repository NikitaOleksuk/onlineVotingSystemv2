package project.onlinevotingsystem.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PollDto {
    private String pollName;
    private boolean anonymous;
    private List<Long> candidateIds;
}
