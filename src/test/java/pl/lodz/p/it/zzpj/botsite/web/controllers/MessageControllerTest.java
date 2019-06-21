package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.util.NestedServletException;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.bots.BotCreationDTO;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    MessageService messageService;

    private Gson gson = new Gson();

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PrincipalProvider principal;

    private ModelMapper realModelMapper = new ModelMapper();

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(messageController)
                .build();
    }

    @Test
    public void createMessageShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        User user = User
                .builder()
                .id(id)
                .build();
        Message message = Message
                .builder()
                .id(id)
                .user(user)
                .content("message")
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(message, MessageDTO.class)).thenReturn(dto);
        when(this.modelMapper.map(dto, Message.class)).thenReturn(message);
        when(this.principal.getUserId()).thenReturn(0L);
        when(messageService.addMessage(any(), any())).thenReturn(message);

        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(messageService).addMessage(any(Long.class),any(Message.class));
    }

    @Test
    public void createMessageShouldNotWorkWhenExceptionIsThrown() throws Exception, UserNotFoundException {
        Long id = 0L;
        User user = User
                .builder()
                .id(id)
                .build();
        Message message = Message
                .builder()
                .id(id)
                .user(user)
                .content(" ")
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(dto, Message.class)).thenReturn(message);
        when(this.principal.getUserId()).thenReturn(1L);
        when(messageService.addMessage(any(), any())).thenThrow(MessageAdditionException.class);

        Assertions.assertThrows(NestedServletException.class, () -> mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk()));

        verify(messageService).addMessage(any(Long.class),any(Message.class));
    }

}
