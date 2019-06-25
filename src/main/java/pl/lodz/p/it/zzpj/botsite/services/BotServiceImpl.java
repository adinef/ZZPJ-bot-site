package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.repositories.BotRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("botService")
public class BotServiceImpl implements BotService {

    private final BotRepository botRepository;
    private final UserRepository userRepository;

    @Autowired
    public BotServiceImpl(BotRepository botRepository, UserRepository userRepository) {
        this.botRepository = botRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Bot findById(Long id) throws BotRetrievalException {
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
    public List<Bot> findAllForUserId(Long id) throws BotRetrievalException {
        try {
            Optional<User> userById = this.userRepository.findById(id);
            User user = userById.orElseThrow(UserNotFoundException::new);
            List<Bot> bots = this.botRepository.findAllByUser(user);
            return bots;
        } catch (final Exception e) {
            throw new BotRetrievalException("Could not retrieve bot by user's ID");
        }
    }

    @Override
    public List<Bot> findAll() throws BotRetrievalException {
        try {
            Iterable<Bot> bots = this.botRepository.findAll();
            List<Bot> botsList = new ArrayList<>();
            for (Bot bot : bots) {
                botsList.add(bot);
            }
            return botsList;
        } catch (final Exception e) {
            throw new BotRetrievalException("Could not retrieve bots", e);
        }
    }

    @Override
    public List<Bot> findAllForName(String name) throws BotRetrievalException {
        try {
            List<Bot> bots = this.botRepository.findAllByName(name);
            return bots;
        } catch (final Exception e) {
            throw new BotRetrievalException("Could not retrieve bots", e);
        }
    }

    @Override
    public Bot addBot(Bot bot) throws BotAdditionException {
        try {
            List<Bot> allByUser = this.botRepository.findAllByUser(bot.getUser());
            if (allByUser
                    .stream()
                    .anyMatch(
                            e ->
                                    ( e.getUser().getLogin() + ":" + e.getName() )
                                            .equals(bot.getUser().getLogin() + ":" + bot.getName())
                    )
            ) {
                throw new BotAlreadyExistsException("Bot for user already exists.");
            }
            return this.botRepository.save(bot);
        } catch (final Exception e){
            throw new BotAdditionException("Error occurred during addition of bot.", e);
        }
    }

    @Override
    public Bot updateBotName(Long id, String name) throws BotUpdateException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            Bot botRetrieved = bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."));
            botRetrieved.setName(name);
            return this.botRepository.save(botRetrieved);
        } catch (final Exception e) {
            throw new BotUpdateException("Could nto update bot", e);
        }
    }

    @Override
    public Bot updateBotChannel(Long id, String channel) throws BotUpdateException {
        try {
            Optional<Bot> bot = this.botRepository.findById(id);
            Bot botRetrieved = bot.orElseThrow(() -> new BotNotFoundException("Bot with that ID not found."));
            botRetrieved.setChannel(channel);
            return this.botRepository.save(botRetrieved);
        } catch (final Exception e) {
            throw new BotUpdateException("Could nto update bot", e);
        }
    }

    @Override
    public void deleteBot(Long id) throws BotDeletionException {
        try {
            this.botRepository.deleteById(id);
        } catch (final Exception e) {
            throw new BotDeletionException("Could not delete bot.", e);
        }
    }
}