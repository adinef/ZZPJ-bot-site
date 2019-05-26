package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.repositories.BotRepository;

import java.util.Optional;

@Service("mongoBotService")
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;

    @Autowired
    public BotServiceImpl(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    @Override
    public Bot findById(String id) throws BotRetrievalException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            return bot
                    .orElseThrow(
                            () -> new BotNotFoundException("Bot with that ID not found.")
                    );
        } catch (final Exception e) {
            throw new BotRetrievalException("Could not retrieve bot by ID", e);
        }
    }

    @Override
    public Bot addBot(Bot bot) throws BotAdditionException {
        if (bot.getId() != null) {
            throw new BotAdditionException("Cannot add element with id specified.");
        }
        try {
            return this.botRepository.save(bot);
        } catch (final Exception e){
            throw new BotAdditionException("Error occurred during addition of bot.", e);
        }
    }

    @Override
    public Bot updateBotName(String id, String name) throws BotNotFoundException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            Bot botRetrieved = bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."));
            botRetrieved.setName(name);
            return this.botRepository.save(botRetrieved);
        } catch (final Exception e) {
            throw new BotNotFoundException("Bot with that ID not found.", e);
        }
    }

    @Override
    public Bot updateBotToken(String id, String token) throws BotNotFoundException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            Bot botRetrieved = bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."));
            botRetrieved.setToken(token);
            return this.botRepository.save(botRetrieved);
        } catch (final Exception e) {
            throw new BotNotFoundException("Bot with that ID not found.", e);
        }
    }

    @Override
    public Bot updateBotChannel(String id, String channel) throws BotNotFoundException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            Bot botRetrieved = bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."));
            botRetrieved.setChannel(channel);
            return this.botRepository.save(botRetrieved);
        } catch (final Exception e) {
            throw new BotNotFoundException("Bot with that ID not found.", e);
        }
    }

    @Override
    public void deleteBot(String id) throws BotDeletionException {
        try {
            this.botRepository.deleteById(id);
        } catch (final Exception e) {
            throw new BotDeletionException("Could not delete bot.", e);
        }
    }
}