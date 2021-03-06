package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;

import java.util.List;

public interface BotService {
    Bot findById(Long id) throws BotRetrievalException;
    Bot addBot(Bot bot) throws BotAlreadyExistsException, BotAdditionException;
    Bot updateBotName(Long id, String name) throws BotUpdateException;
    Bot updateBotChannel(Long id, String channel) throws BotUpdateException;
    void deleteBot(Long id) throws BotDeletionException;
    List<Bot> findAllForUserId(Long id) throws BotRetrievalException;
    List<Bot> findAll() throws BotRetrievalException;
    List<Bot> findAllForName(String name) throws BotRetrievalException;
}
