package pl.lodz.p.it.zzpj.botsite.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.events.OnUserRegistrationCompleteEvent;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

    @Autowired
    UserService userService;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {

        this.confirmRegistration(onUserRegistrationCompleteEvent);

    }

    private void confirmRegistration(OnUserRegistrationCompleteEvent event) {

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/api/user/verifyAccount?token=" + token;
        String message = "Welcome to Botsite, confirm your registration: " + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("whatever7@hmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);

    }
}
