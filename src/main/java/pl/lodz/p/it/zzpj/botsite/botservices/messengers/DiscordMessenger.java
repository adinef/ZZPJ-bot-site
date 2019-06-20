package pl.lodz.p.it.zzpj.botsite.botservices.messengers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;

public class DiscordMessenger extends AbstractBotMessengerBase {

    public static BotMessenger of(Bot bot, RestTemplate restTemplate) {
        return new DiscordMessenger(bot, restTemplate);
    }

    private DiscordMessenger(Bot bot, RestTemplate restTemplate) {
        super(bot, restTemplate);
    }

    @Override
    public void sendMessage(Message message) {
        this.post(bot.getChannel(), new DiscordMessage(message.getContent(), bot.getName()));
    }

    private class DiscordMessage {
        @JsonProperty("content")
        private String content;

        @JsonProperty("username")
        private String username;

        DiscordMessage(String content, String username) {
            this.content = content;
            this.username = username;
        }
    }
}
