package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTask {
    @Id
    private String id;
    @DBRef
    private String botId;
    @DBRef
    private String messageId;
    @DBRef
    private String userId;
    /**
     * Date of task creation
     */
    private LocalDateTime creationDate;
    /**
     * Date of reminder/sending message
     */
    private LocalDateTime reminderDate;
    private boolean isRepeatable;
    private boolean isDone;
}
