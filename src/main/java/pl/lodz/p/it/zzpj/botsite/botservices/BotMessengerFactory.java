package pl.lodz.p.it.zzpj.botsite.botservices;

import pl.lodz.p.it.zzpj.botsite.botservices.exception.MessengerDoesntExistException;
import pl.lodz.p.it.zzpj.botsite.botservices.messengers.BotMessenger;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;

public interface BotMessengerFactory {
    BotMessenger getForBot(Bot bot) throws MessengerDoesntExistException;
}
