package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.config.errorhandling.RestResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.BotDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.BotNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.BotRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.BotUpdateException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BotAdminControllerTest {

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
    private BotAdminController botController;

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
        when(this.userService.findById(any())).thenReturn(new User());
        when(this.botService.addBot(any())).thenReturn(bot);

        mockMvc.perform(
                post("/api/admin/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());
        verify(botService).addBot(any(Bot.class));
    }

    @Test
    public void createBotShouldGiveBadRequestOnUserDoesntExist() throws Exception {
        Bot bot = Bot.builder().id(1L).name("FirstBot").channel("FirstChannel").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(dto, Bot.class)).thenReturn(new Bot());
        when(this.userService.findById(any())).thenThrow(UserRetrievalException.class);

        mockMvc.perform(
                post("/api/admin/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBotShouldGiveBadRequestOnAlreadyExists() throws Exception {
        Bot bot = Bot.builder().id(1L).name("FirstBot").channel("FirstChannel").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(this.userService.findById(any())).thenReturn(new User());
        when(botService.addBot(any())).thenThrow(BotAlreadyExistsException.class);

        mockMvc.perform(
                post("/api/admin/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
        )
                .andExpect(status().isBadRequest());
        verify(botService).addBot(any(Bot.class));
    }

    @Test
    public void createBotShouldGiveBadRequestWhenAdditionFailed() throws Exception {
        Bot bot = Bot.builder().id(1L).name("FirstBot").channel("FirstChannel").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(this.botService.addBot(any())).thenThrow(BotAdditionException.class);
        when(this.userService.findById(any())).thenReturn(new User());

        mockMvc.perform(
                post("/api/admin/bot/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void editBotShouldGiveBadRequestIfBotByIdDoesntExist() throws Exception {
        Bot bot = Bot.builder().id(1L).name("FirstBot").channel("FirstChannel").build();
        BotEditDTO dto = this.realModelMapper.map(bot, BotEditDTO.class);
        String json = gson.toJson(dto);

        when(this.botService.findById(1L)).thenThrow(BotRetrievalException.class);

        mockMvc.perform(
                put("/api/admin/bot/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void editBotShouldGiveBadRequestWhenUnknownUpdateExceptionOccurs() throws Exception {
        Bot bot = Bot.builder().id(1L).name("FirstBot").channel("FirstChannel").build();
        BotEditDTO dto = this.realModelMapper.map(bot, BotEditDTO.class);
        String json = gson.toJson(dto);

        when(this.botService.findById(1L)).thenReturn(bot);
        when(this.botService.updateBotName(1L, dto.getName())).thenThrow(BotUpdateException.class);

        mockMvc.perform(
                put("/api/admin/bot/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    //
    @Test
    public void editBotShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        User user = User.builder().login("test1").build();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").user(user).build();
        BotEditDTO dto = this.realModelMapper.map(bot, BotEditDTO.class);
        String json = gson.toJson(dto);

        when(this.botService.findById(1L)).thenReturn(bot);
        when(this.botService.updateBotChannel(any(), anyString())).thenReturn(bot);
        when(this.botService.updateBotName(any(), anyString())).thenReturn(bot);
        when(this.modelMapper.map(bot, BotViewDTO.class)).thenReturn(new BotViewDTO());

        mockMvc.perform(
                put("/api/admin/bot/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteBotShouldReturnBadRequesWhenErrorOccurs() throws Exception {
        doThrow(BotDeletionException.class).when(this.botService).deleteBot(any());
        mockMvc.perform(
                delete("/api/admin/bot/1")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBotShouldWorkAsExpected() throws Exception {
        doNothing().when(this.botService).deleteBot(1L);
        mockMvc.perform(
                delete("/api/admin/bot/1")
        ).andExpect(status().isOk());
    }

    @Test
    public void getAllReturnsEmptyResultWhenNotFound() throws Exception {
        when(this.botService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(
                get("/api/admin/bot")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    public void getAllReturnsRightAmountWhenFound() throws Exception {
        Bot bot1 = Bot.builder().id(1L).channel("A").build();
        Bot bot2 = Bot.builder().id(2L).channel("B").build();
        when(this.botService.findAll()).thenReturn(
                Arrays.asList(bot1, bot2)
        );
        when(this.modelMapper.map(bot1, BotViewDTO.class)).thenReturn(BotViewDTO.builder().id(1L).build());
        when(this.modelMapper.map(bot2, BotViewDTO.class)).thenReturn(BotViewDTO.builder().id(2L).build());
        mockMvc.perform(
                get("/api/admin/bot")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray());

    }
}
