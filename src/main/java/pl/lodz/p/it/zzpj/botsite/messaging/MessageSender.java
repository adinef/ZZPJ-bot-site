package pl.lodz.p.it.zzpj.botsite.messaging;

import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;

public interface MessageSender {
    boolean send(Bot bot, Message message);
}
