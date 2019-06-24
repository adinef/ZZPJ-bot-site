package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

    private MockMvc mockMvc;

    @Mock
    MessageService messageService;

    @Mock
    UserService userService;

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
                .content("message")
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);

        when(this.modelMapper.map(message, MessageDTO.class)).thenReturn(dto);
        when(this.userService.findByLogin(any())).thenReturn(user);
        when(this.modelMapper.map(dto, Message.class)).thenReturn(message);
        when(messageService.addMessage(any())).thenReturn(message);

        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isCreated());

        verify(messageService).addMessage(any());
    }


    @Test
    public void getAllMessagesForCurrentUserShouldWorkAsExpected() throws Exception {
        List<Message> messages = new ArrayList<>();
        User user = User.builder()
                .id(1L)
                .build();
        messages.add(Message.builder()
                .id(1L)
                .content("blabla")
                .user(user)
                .build());
        messages.add(Message.builder()
                .id(2L)
                .content("blabla")
                .user(user)
                .build());
        when(principal.getUserId()).thenReturn(user.getId());
        when(messageService.findAllByUserId(any())).thenReturn(messages);
        List<MessageDTO> dtos = new ArrayList<>();
        this.realModelMapper.map(messages, dtos);
        String json = gson.toJson(dtos);
        mockMvc.perform(
                get("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isOk());
        verify(messageService).findAllByUserId(any(Long.class));
    }

    @Test
    public void getMessageByIdForCurrentUserShouldWorkAsExpected() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();
        Message message = Message.builder()
                .id(1L)
                .content("blabla")
                .user(user)
                .build();
        when(principal.getUserId()).thenReturn(user.getId());
        when(messageService.findById(any())).thenReturn(message);
        mockMvc.perform(
                get("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("")
        ).andExpect(status().isOk());
        verify(messageService).findById(any(Long.class));
    }

    @Test
    public void editMessageShouldWorkAsExpected() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();
        Message editedMessage = Message.builder()
                .id(2L)
                .content("blab")
                .user(user)
                .build();
        MessageDTO dto = this.realModelMapper.map(editedMessage, MessageDTO.class);
        String json = gson.toJson(dto);
        when(principal.getUserId()).thenReturn(user.getId());
        when(messageService.findById(any())).thenReturn(editedMessage);
        when(messageService.updateMessage(editedMessage)).thenReturn(editedMessage);
        mockMvc.perform(
                put("/api/messages/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isOk());

        verify(messageService).updateMessage(any());
    }

    @Test
    public void deleteMessageShouldWorkAsExpected() throws Exception {
        List<Message> messages = new ArrayList<>();
        User user = User.builder()
                .id(1L)
                .build();
        Message toBeDeleted = Message.builder()
                .id(1L)
                .content("blabla")
                .user(user)
                .build();
        messages.add(toBeDeleted);
        messages.add(Message.builder()
                .id(2L)
                .content("blabla")
                .user(user)
                .build());
        doAnswer(invocation -> {
            Object arg1 = invocation.getArgument(0);
            Message mes = messages.stream().filter(m ->
                    m.getId().longValue() == ((Message) arg1).getId().longValue()).findFirst().get();
            messages.remove(mes);
            return null;
        }).when(messageService).deleteMessage(any());
        when(messageService.findById(any())).thenReturn(toBeDeleted);
        when(principal.getUserId()).thenReturn(user.getId());
        MessageDTO dto = this.realModelMapper.map(toBeDeleted, MessageDTO.class);
        String json = gson.toJson(dto);
        Assertions.assertTrue(!json.isEmpty());
        mockMvc.perform(
                delete("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        verify(messageService).deleteMessage(any());
        Assertions.assertEquals(1, messages.size());
    }
}
