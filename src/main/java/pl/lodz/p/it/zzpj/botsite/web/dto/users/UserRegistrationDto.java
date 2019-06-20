package pl.lodz.p.it.zzpj.botsite.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {
    private String login;
    private String email;
    private String password;
}
