package pl.lodz.p.it.zzpj.botsite.botservices.messengers;

import pl.lodz.p.it.zzpj.botsite.entities.Message;

public interface BotMessenger {
    void sendMessage(Message message);
}
