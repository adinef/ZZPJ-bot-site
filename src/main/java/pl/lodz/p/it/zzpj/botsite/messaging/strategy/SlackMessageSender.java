package pl.lodz.p.it.zzpj.botsite.messaging.strategy;

import lombok.Getter;
import lombok.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.messaging.MessageSender;

public class SlackMessageSender implements MessageSender {

    private final RestTemplate restTemplate;

    public SlackMessageSender(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean send(Bot bot, Message message) {
        String endpoint = bot.getChannel();
        SlackMessage slackMessage = SlackMessage.of(message.getContent());
        HttpEntity<SlackMessage> body = new HttpEntity<>(slackMessage);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(endpoint, body, String.class);
        if (stringResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return true;
        }
        return false;
    }


    @Getter
    @Value(staticConstructor = "of")
    private class SlackMessage {
        private String text;
    }
}
