package pl.lodz.p.it.zzpj.botsite.botservices.messengers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;

public class SlackMessenger extends AbstractBotMessengerBase {

    public static BotMessenger of(Bot bot, RestTemplate restTemplate) {
        return new SlackMessenger(bot, restTemplate);
    }

    private SlackMessenger(Bot bot, RestTemplate restTemplate) {
        super(bot, restTemplate);
    }

    @Override
    public void sendMessage(Message message) {
        String channel = this.bot.getChannel();
         this.post(channel, new SlackMessage(message.getContent()));
    }

    private class SlackMessage {

        @JsonProperty("text")
        private String message;

        private SlackMessage(String message) {
            this.message = message;
        }
    }
}
