package pl.lodz.p.it.zzpj.botsite.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDto {
    String login;
    String email;
    String password;
}
