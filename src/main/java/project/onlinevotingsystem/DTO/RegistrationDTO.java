package project.onlinevotingsystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationDTO {
    private Long id;
    private String username;
    private String email;
    private String token;

}
