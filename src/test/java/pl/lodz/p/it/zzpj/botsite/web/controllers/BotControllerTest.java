package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.BotCreationDTO;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private ModelMapper realModelMapper = new ModelMapper();

    @InjectMocks
    private BotController botController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(botController)
                .build();
    }

    @Test
    public void createBotShouldWorkAsExpected() throws Exception {

        Long id = 0L;
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(bot, BotCreationDTO.class)).thenReturn(dto);
        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(this.userService.findByLogin(anyString())).thenReturn(new User());
        when(this.principal.getName()).thenReturn("");
        when(botService.addBot(any())).thenReturn(bot);

        mockMvc.perform(
                post("/api/bot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(botService).addBot(any(Bot.class));

    }

}
