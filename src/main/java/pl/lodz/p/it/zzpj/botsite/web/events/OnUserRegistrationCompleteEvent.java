package pl.lodz.p.it.zzpj.botsite.web.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import pl.lodz.p.it.zzpj.botsite.entities.User;

@Getter
public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String appUrl;

    public OnUserRegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}
