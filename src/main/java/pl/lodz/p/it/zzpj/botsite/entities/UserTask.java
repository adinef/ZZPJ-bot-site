package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTask {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Bot bot;

    @ManyToOne
    private Message message;

    @NotNull
    @ManyToOne
    private User user;
    /**
     * Date of task creation
     */
    @NotNull
    private LocalDateTime creationDate;
    /**
     * Date of reminder/sending message
     */
    @NotNull
    private LocalDateTime reminderDate;
    @NotNull
    private boolean isRepeatable;
    @NotNull
    private boolean isDone;
}
