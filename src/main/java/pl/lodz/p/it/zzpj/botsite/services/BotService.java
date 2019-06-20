package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;

public interface BotService {
    Bot findById(Long id) throws BotRetrievalException;
    Bot addBot(Bot bot) throws BotAlreadyExistsException, BotAdditionException;
    Bot updateBotName(Long id, String name) throws BotNotFoundException;
    Bot updateBotChannel(Long id, String channel) throws BotNotFoundException;
    void deleteBot(Long id) throws BotNotFoundException, BotRetrievalException, BotDeletionException;
}
