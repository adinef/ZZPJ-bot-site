package pl.lodz.p.it.zzpj.botsite.web.events;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import pl.lodz.p.it.zzpj.botsite.entities.User;

@Getter
public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

    private User user;

    public OnUserRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }

}
