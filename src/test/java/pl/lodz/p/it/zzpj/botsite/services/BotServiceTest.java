package pl.lodz.p.it.zzpj.botsite.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.repositories.BotRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotServiceTest {

    @Autowired
    private BotService botService;

    @Mock
    private BotRepository botRepository;

    @BeforeEach
    public void setup() {
        this.botService = new BotServiceImpl(botRepository);
    }

    @Test
    void findByIdShouldReturnEntityWhenExpected() throws BotRetrievalException {
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        Assertions.assertEquals("FirstBot", botService.findById(id).getName());
    }

    @Test
    void findByIdShouldThrowRightExceptionAfterDatabaseRuntimeException() {
        when(botRepository.findById(anyString())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findById("test"));
    }

    @Test
    void findByIdShouldThrowRightExceptionWhenUserEmpty() {
        when(botRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(BotRetrievalException.class, () -> botService.findById("test"));
    }

    @Test
    void addBotShouldAddBot() throws BotAlreadyExistsException {
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        botService.addBot(bot);
        verify(botRepository).save(bot);
    }

    @Test
    void addBotShouldThrowBotAlreadyExistsException(){
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        Assertions.assertThrows(BotAlreadyExistsException.class, () -> botService.addBot(bot));
    }

    @Test
    void updateBotNameShouldUpdateBotName() throws BotRetrievalException, BotNotFoundException {
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        botService.updateBotName(id,"NewName");
        Assertions.assertEquals(bot.getName(), "NewName");
    }

    @Test
    void updateBotNameShouldThrowBotNotFoundException(){
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        Assertions.assertThrows(BotNotFoundException.class, () -> botService.updateBotName(id,"NewName"));
    }

    @Test
    void updateBotTokenShouldUpdateBotToken() throws BotNotFoundException{
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        botService.updateBotToken(id,"NewToken");
        Assertions.assertEquals(bot.getToken(), "NewToken");
    }

    @Test
    void updateBotTokenShouldThrowBotNotFoundException(){
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        Assertions.assertThrows(BotNotFoundException.class, () -> botService.updateBotToken(id,"NewToken"));
    }

    @Test
    void updateBotChannelShouldUpdateBotChannel() throws BotNotFoundException {
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        botService.updateBotChannel(id,"NewChannel");
        Assertions.assertEquals(bot.getChannel(), "NewChannel");
    }

    @Test
    void updateBotChannelShouldThrowBotNotFoundException(){
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        Assertions.assertThrows(BotNotFoundException.class, () -> botService.updateBotChannel(id,"NewChannel"));
    }

    @Test
    void deleteBotShouldDeleteBot() throws BotNotFoundException, BotRetrievalException {
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        when(botRepository.findById(id)).thenReturn(Optional.of(bot));
        botService.deleteBot(id);
        verify(botRepository).delete(bot);
    }

    @Test
    void deleteBotShouldThrowBotNotFoundException(){
        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        Assertions.assertThrows(BotNotFoundException.class, () -> botService.deleteBot(id));
    }

}