package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String login;

    private String name;

    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Bot> bots;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserTask> userTasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Message> messages;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<VerificationTokenInfo> verificationTokenInfos;
}
