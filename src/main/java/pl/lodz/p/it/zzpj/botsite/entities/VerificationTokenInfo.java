package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class VerificationTokenInfo {

    private static final int EXPIRATION_TIME = 60 * 24;

    @Id
    @GeneratedValue
    private String token;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    private Date expirationDate;

    public VerificationTokenInfo(User user, String token) {
        this.user = user;
        this.token = token;
    }

    private Date calculateExpirationDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
