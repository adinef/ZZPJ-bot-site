package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.repositories.BotRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotServiceTest {

    @Autowired
    private BotService botService;

    @Mock
    private BotRepository botRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        this.botService = new BotServiceImpl(botRepository, userRepository);
    }

    @Test
    void findByIdShouldReturnEntityWhenExpected() throws BotRetrievalException {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        Assertions.assertEquals("FirstBot", botService.findById(id).getName());
    }

    @Test
    void findByIdShouldThrowRightExceptionAfterDatabaseRuntimeException() {
        when(botRepository.findById(any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findById(1L));
    }

    @Test
    void findByIdShouldThrowRightExceptionWhenUserEmpty() {
        when(botRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findById(1L));
    }
//
    @Test
    void findByUserIdShouldReturnEntityWhenExpected() throws BotRetrievalException {
        Long botId = 0L;
        Bot bot = Bot.builder().id(botId).name("FirstBot").channel("FirstChannel").build();
        User user = User.builder().id(1L).build();
        when(botRepository.findAllByUser(user)).thenReturn(Collections.singletonList(bot));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Assertions.assertFalse(botService.findAllForUserId(1L).isEmpty());
    }

    @Test
    void findByUserIdShouldThrowRightExceptionAfterDatabaseRuntimeException() {
        when(botRepository.findById(any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findById(1L));
    }

    @Test
    void findByUserIdShouldThrowRightExceptionWhenUserNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findAllForUserId(1L));
    }

    @Test
    void findByUserIdShouldThrowRightExceptionWhenNoBotsFound() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(botRepository.findAllByUser(any())).thenReturn(Collections.emptyList());
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findAllForUserId(1L));
    }


    @Test
    void addBotShouldAddBot() throws BotAlreadyExistsException, BotAdditionException {
        Bot bot = Bot.builder().name("FirstBot").channel("FirstChannel").build();
        botService.addBot(bot);
        verify(botRepository).save(bot);
    }

    @Test
    void addBotShouldThrowBotAlreadyExistsException(){
        Bot bot = Bot.builder().id(1L).name("FirstBot").channel("FirstChannel").build();
        Assertions.assertThrows(BotAdditionException.class, () -> botService.addBot(bot));
    }

    @Test
    void updateBotNameShouldUpdateBotName() throws BotRetrievalException, BotNotFoundException {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        botService.updateBotName(id,"NewName");
        Assertions.assertEquals(bot.getName(), "NewName");
    }

    @Test
    void updateBotNameShouldThrowBotNotFoundException(){
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        Assertions.assertThrows(BotNotFoundException.class, () -> botService.updateBotName(id,"NewName"));
    }

    @Test
    void updateBotChannelShouldUpdateBotChannel() throws BotNotFoundException {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        botService.updateBotChannel(id,"NewChannel");
        Assertions.assertEquals(bot.getChannel(), "NewChannel");
    }

    @Test
    void updateBotChannelShouldThrowBotNotFoundException(){
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        Assertions.assertThrows(BotNotFoundException.class, () -> botService.updateBotChannel(id,"NewChannel"));
    }

    @Test
    void deleteBotShouldDeleteBot() throws BotNotFoundException, BotRetrievalException, BotDeletionException {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        botService.deleteBot(id);
        verify(botRepository).deleteById(bot.getId());
    }

    @Test
    void deleteBotShouldThrowBotDeletionException(){
        Long id = 0L;
        doThrow(RuntimeException.class).when(botRepository).deleteById(id);
        Assertions.assertThrows(BotDeletionException.class, () -> botService.deleteBot(id));
    }

}