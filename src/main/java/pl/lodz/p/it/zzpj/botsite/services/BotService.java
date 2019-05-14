package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotRetrievalException;

public interface BotService {
    Bot findById(String id) throws BotRetrievalException;
    void addBot(Bot bot) throws BotAlreadyExistsException;
    void updateBotName(String id, String name) throws BotNotFoundException;
    void updateBotToken(String id, String token) throws BotNotFoundException;
    void updateBotChannel(String id, String channel) throws BotNotFoundException;
    void deleteBot(String id) throws BotNotFoundException, BotRetrievalException;
}
