package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
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
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import pl.lodz.p.it.zzpj.botsite.config.errorhandling.RestResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.web.dto.BotCreationDTO;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BotControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BotController botController;

    @Mock
    BotService botService;

    private Gson gson = new Gson();

    @Mock
    private ModelMapper modelMapper;

    private ModelMapper realModelMapper = new ModelMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(botController)
                .build();
    }

    @Test
    public void createBotShouldWorkAsExpected() throws Exception {

        String id = ObjectId.get().toString();
        Bot bot = Bot.builder().id(id).name("FirstBot").channel("FirstChannel").token("FirstToken").build();
        BotCreationDTO dto = this.realModelMapper.map(bot, BotCreationDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(bot, BotCreationDTO.class)).thenReturn(dto);
        when(this.modelMapper.map(dto, Bot.class)).thenReturn(bot);
        when(botService.addBot(any())).thenReturn(bot);

        mockMvc.perform(
                post("/api/bot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(botService).addBot(any(Bot.class));

    }

}
