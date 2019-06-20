package pl.lodz.p.it.zzpj.botsite.eventListeners;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.services.VerificationTokenService;
import pl.lodz.p.it.zzpj.botsite.utils.TokenGenerator;
import pl.lodz.p.it.zzpj.botsite.web.events.OnUserRegistrationCompleteEvent;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

    private static final String ACTIVATE_ENDPOINT = "/api/user/activate";

    private final VerificationTokenService verificationTokenService;

    private final JavaMailSender mailSender;

    private final TokenGenerator tokenGenerator;

    @Value("${application.url}")
    String appUrl;

    public RegistrationListener(TokenGenerator tokenGenerator,
                                VerificationTokenService verificationTokenService,
                                JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;
        this.mailSender = mailSender;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {
        this.confirmRegistration(onUserRegistrationCompleteEvent);
    }

    private void confirmRegistration(OnUserRegistrationCompleteEvent event) {

        User user = event.getUser();
        try {
            String generatedToken = tokenGenerator.generate();
            verificationTokenService.saveToken(user, generatedToken);
            String recipientAddress = user.getEmail();
            String subject = "Registration Confirmation";
            String confirmationUrl
                    = appUrl + ACTIVATE_ENDPOINT + "?token=" + generatedToken;
            String message = "Welcome to Botsite, confirm your registration: " + confirmationUrl;

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("whatever7@hmail.com");
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message);
            mailSender.send(email);


        } catch (UserRetrievalException e) {
            e.printStackTrace();
        }


    }
}
