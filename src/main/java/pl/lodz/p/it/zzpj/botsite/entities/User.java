package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User  {
    @Id
    private String login;
    private String name;
    private String lastName;
    @Email
    private String email;
    private String password;
    private boolean active = false;
    private List<UserRole> roles = new ArrayList<>();
}
