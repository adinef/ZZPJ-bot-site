package pl.lodz.p.it.zzpj.botsite.botservices.messengers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;

public abstract class AbstractBotMessengerBase implements BotMessenger {

    private RestTemplate restTemplate;
    protected final Bot bot;

    AbstractBotMessengerBase(Bot bot, RestTemplate restTemplate) {
        this.bot = bot;
        this.restTemplate = restTemplate;
    }

    final void post(String URL, Object object) {
        this.restTemplate.postForLocation(URL, object);
    }
}
