package pl.lodz.p.it.zzpj.botsite.botservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.it.zzpj.botsite.exceptions.services.MessengerDoesntExistException;
import pl.lodz.p.it.zzpj.botsite.botservices.messengers.BotMessenger;
import pl.lodz.p.it.zzpj.botsite.botservices.messengers.DiscordMessenger;
import pl.lodz.p.it.zzpj.botsite.botservices.messengers.SlackMessenger;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;

@Component
public class BotMessengerFactoryImpl implements BotMessengerFactory{

    private final RestTemplate restTemplate;

    @Autowired
    public BotMessengerFactoryImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BotMessenger getForBot(Bot bot) throws MessengerDoesntExistException {
        if (bot.getName().equalsIgnoreCase("slack")) {
            return SlackMessenger.of(bot, restTemplate);
        } else if (bot.getName().equalsIgnoreCase("discord")) {
            return DiscordMessenger.of(bot, restTemplate);
        } else {
            throw new MessengerDoesntExistException();
        }
    }

}
