package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.Data;
import lombok.Generated;
import pl.lodz.p.it.zzpj.botsite.utils.TokenGenerator;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class VerificationTokenInfo {

    private static final int EXPIRATION_TIME = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String token;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    private LocalDateTime expirationTime;

    public VerificationTokenInfo() {
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
    }

    public VerificationTokenInfo(User user, String token) {
        this();
        this.user = user;
        this.token = token;
    }

    private LocalDateTime calculateExpirationTime(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }
}
