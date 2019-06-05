package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User  {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String login;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private boolean active = false;

    @NotNull
    @ElementCollection
    private List<UserRole> roles = new ArrayList<>();

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Bot> bots;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserTask> userTasks;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Message> messages;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<VerificationTokenInfo> verificationTokenInfos;
}
