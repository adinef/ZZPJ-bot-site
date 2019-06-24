package pl.lodz.p.it.zzpj.botsite.web.dto.usertasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserTaskUserDTO {
    private Long messageId;
    private Long botId;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime reminderDate;
    private boolean isRepeatable;
    private boolean isDone;
}
