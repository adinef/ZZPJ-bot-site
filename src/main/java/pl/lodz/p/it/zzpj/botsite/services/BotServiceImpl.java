package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.*;
import pl.lodz.p.it.zzpj.botsite.repositories.BotRepository;

import java.util.Optional;

@Service("mongoBotService")
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;

    @Autowired
    public BotServiceImpl(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    public Bot findById(String id) throws BotRetrievalException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            return bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."));
        } catch (final Exception e) {
            throw new BotRetrievalException("Could not retrieve bot by ID", e);
        }
    }

    public void addBot(Bot bot) throws BotAlreadyExistsException {
        if (!this.botRepository.findById(bot.getId()).isPresent()) {
            this.botRepository.save(bot);
        } else {
            throw new BotAlreadyExistsException("Bot with this ID is already taken");
        }
    }

    public void updateBotName(String id, String name) throws BotNotFoundException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            (bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."))).setName(name);
            this.botRepository.save(bot.get());
        } catch (final Exception e) {
            throw new BotNotFoundException("Bot with that ID not found.", e);
        }
    }

    public void updateBotToken(String id, String token) throws BotNotFoundException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            (bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."))).setToken(token);
            this.botRepository.save(bot.get());
        } catch (final Exception e) {
            throw new BotNotFoundException("Bot with that ID not found.", e);
        }
    }

    public void updateBotChannel(String id, String channel) throws BotNotFoundException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            (bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."))).setChannel(channel);
            this.botRepository.save(bot.get());
        } catch (final Exception e) {
            throw new BotNotFoundException("Bot with that ID not found.", e);
        }
    }

    public void deleteBot(String id) throws BotNotFoundException, BotRetrievalException {
        if (!this.botRepository.findById(id).isPresent()) {
            throw new BotNotFoundException("Bot with that ID not found.");
        } else {
            this.botRepository.delete(findById(id));
        }
    }
}