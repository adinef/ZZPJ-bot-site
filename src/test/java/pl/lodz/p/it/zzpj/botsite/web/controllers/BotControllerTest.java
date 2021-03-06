package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import pl.lodz.p.it.zzpj.botsite.config.errorhandling.RestResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.BotAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotCreationDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotEditDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotViewDTO;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BotControllerTest {

    private MockMvc mockMvc;

    @Mock
    BotService botService;

    private Gson gson = new Gson();

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @Mock
    private PrincipalProvider principal;


    private RestResponseEntityExceptionHandler exceptionHandler = new RestResponseEntityExceptionHandler(HttpHeaders.EMPTY);


    private ModelMapper realModelMapper = new ModelMapper();

    @InjectMocks
    private BotController botController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(botController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    public void createBotShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(bot, BotCreationDTO.class)).thenReturn(dto);
        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(this.userService.findByLogin(anyString())).thenReturn(new User());
        when(this.principal.getName()).thenReturn("");
        when(botService.addBot(any())).thenReturn(bot);

        mockMvc.perform(
                post("/api/bot/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());
        verify(botService).addBot(any(Bot.class));
    }

    @Test
    public void createBotShouldGiveBadRequestOnAlreadyExists() throws Exception {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(this.userService.findByLogin(anyString())).thenReturn(new User());
        when(this.principal.getName()).thenReturn("");
        when(botService.addBot(any())).thenThrow(BotAlreadyExistsException.class);

        mockMvc.perform(
                post("/api/bot/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(botService).addBot(any(Bot.class));
    }

    @Test
    public void createBotShouldGiveBadRequestWhenUserDoesntExist() throws Exception {
        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(this.userService.findByLogin(anyString())).thenThrow(UserRetrievalException.class);
        when(this.principal.getName()).thenReturn("");

        mockMvc.perform(
                post("/api/bot/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void editBotShouldGiveBadRequestWhenBotDoesntBelongToUser() throws Exception {
        Long id = 0L;
        User user = User.builder().login("test1").build();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").user(user).build();
        BotEditDTO dto = this.realModelMapper.map(bot, BotEditDTO.class);
        String json = gson.toJson(dto);

        when(this.botService.findById(1L)).thenReturn(bot);
        when(this.principal.getName()).thenReturn("test2");

        mockMvc.perform(
                put("/api/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBotShouldGiveBadRequestWhenBotDoesntBelongToUser() throws Exception {
        when(this.botService.findById(1L)).thenReturn(Bot.builder().user(User.builder().login("test").build()).build());
        when(this.principal.getName()).thenReturn("test2");
        mockMvc.perform(
                delete("/api/bot/user/1")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBotShouldWorkAsExpected() throws Exception {
        when(this.botService.findById(1L)).thenReturn(Bot.builder().id(1L).user(User.builder().login("test").build()).build());
        when(this.principal.getName()).thenReturn("test");
        doNothing().when(this.botService).deleteBot(1L);
        mockMvc.perform(
                delete("/api/bot/user/1")
        ).andExpect(status().isOk());
    }

    @Test
    public void editBotShouldGiveBadRequestWhenBotAlreadyExists() throws Exception {
        Long id = 0L;
        User user = User.builder().login("test1").build();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").user(user).build();
        BotEditDTO dto = this.realModelMapper.map(bot, BotEditDTO.class);
        String json = gson.toJson(dto);

        when(this.botService.findById(1L)).thenReturn(bot);
        when(this.principal.getName()).thenReturn("test2");

        mockMvc.perform(
                put("/api/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void editBotShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        User user = User.builder().login("test1").build();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").user(user).build();
        BotEditDTO dto = this.realModelMapper.map(bot, BotEditDTO.class);
        String json = gson.toJson(dto);

        when(this.botService.findById(1L)).thenReturn(bot);
        when(this.principal.getName()).thenReturn("test1");
        when(this.botService.updateBotChannel(any(), anyString())).thenReturn(bot);
        when(this.botService.updateBotName(any(), anyString())).thenReturn(bot);

        mockMvc.perform(
                put("/api/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    public void getAllForUserReturnsEmptyResultWhenNotFound() throws Exception {
        when(this.principal.getName()).thenReturn("");
        when(this.botService.findAllForUserId(any())).thenReturn(Collections.emptyList());
        when(this.userService.findByLogin("")).thenReturn(User.builder().id(1L).build());
        mockMvc.perform(
                get("/api/bot/user")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    public void getAllForUserReturnsRightAmountWhenFound() throws Exception {
        Bot bot1 = Bot.builder().id(1L).channel("A").build();
        Bot bot2 = Bot.builder().id(2L).channel("B").build();
        when(this.principal.getName()).thenReturn("");
        when(this.botService.findAllForUserId(1L)).thenReturn(
                Arrays.asList(bot1, bot2)
        );
        when(this.userService.findByLogin("")).thenReturn(User.builder().id(1L).build());
        when(this.modelMapper.map(bot1, BotViewDTO.class)).thenReturn(BotViewDTO.builder().id(1L).build());
        when(this.modelMapper.map(bot2, BotViewDTO.class)).thenReturn(BotViewDTO.builder().id(2L).build());
        mockMvc.perform(
                get("/api/bot/user")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray());

    }

}
