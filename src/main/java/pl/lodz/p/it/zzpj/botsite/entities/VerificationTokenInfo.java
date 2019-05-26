package pl.lodz.p.it.zzpj.botsite.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Document
@Data
public class VerificationTokenInfo {

    private static final int EXPIRATION_TIME = 60 * 24;

    @Id
    private String token;

    private User user;
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
