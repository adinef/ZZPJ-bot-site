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
public class MessageAdminControllerTest {

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
    private MessageAdminController messageController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(messageController)
                .build();
    }

    @Test
    public void getMessageByIdForUserShouldWorkAsExpected() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();
        Message message = Message.builder()
                .id(1L)
                .content("blabla")
                .user(user)
                .build();
        when(messageService.getSingleMessageForUserById(any(),any())).thenReturn(message);
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        mockMvc.perform(
                get("/api/messages/user/1/message/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("")
        ).andExpect(status().isOk());
        verify(messageService).getSingleMessageForUserById(any(Long.class),any(Long.class));
    }



    @Test
    public void getAllMessagesForUserShouldWorkAsExpected() throws Exception {
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
        when(messageService.getAllByUserId(any())).thenReturn(messages);
        List<MessageDTO> dtos = new ArrayList<>();
        this.realModelMapper.map(messages, dtos);
        String json = gson.toJson(dtos);
        mockMvc.perform(
                get("/api/messages/user/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isOk());
        verify(messageService).getAllByUserId(any(Long.class));
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
        when(messageService.updateMessage(user.getId(), 2L, "blab")).thenReturn(editedMessage);
        mockMvc.perform(
                put("/api/messages/user/1/message/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isOk());

        verify(messageService).updateMessage(any(Long.class), any(Long.class), any(String.class));
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
            Object arg1 = invocation.getArgument(1);
            Message mes = messages.stream().filter(m ->
                    m.getId().longValue() == ((Long) arg1).longValue()).findFirst().get();
            Assertions.assertFalse(mes == null);
            messages.remove(mes);
            return null;
        }).when(messageService).deleteMessage(any(Long.class), any(Long.class));
        MessageDTO dto = this.realModelMapper.map(toBeDeleted, MessageDTO.class);
        String json = gson.toJson(dto);
        System.out.println("json:");
        System.out.println(json);
        Assertions.assertTrue(!json.isEmpty());
        mockMvc.perform(
                delete("/api/messages/user/1/message/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
        verify(messageService).deleteMessage(any(Long.class), any(Long.class));
        Assertions.assertEquals(1, messages.size());
    }
}
