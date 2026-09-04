package project.onlinevotingsystem.DTO;

import lombok.Data;

@Data
public class CandidateResponseDto {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private String username;
}
