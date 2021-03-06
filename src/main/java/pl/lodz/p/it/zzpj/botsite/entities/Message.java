package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @NotBlank
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message")
    private List<UserTask> userTasks;
}
