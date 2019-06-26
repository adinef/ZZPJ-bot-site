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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.config.errorhandling.RestResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;

import java.util.ArrayList;
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

    private RestResponseEntityExceptionHandler exceptionHandler = new RestResponseEntityExceptionHandler(HttpHeaders.EMPTY);

    private Gson gson = new Gson();

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PrincipalProvider principal;

    private ModelMapper realModelMapper = new ModelMapper();

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(messageController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void addMessageShouldWorkAsExpected() throws Exception {
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
        when(this.principal.getUser()).thenReturn(user);
        when(this.modelMapper.map(message, MessageDTO.class)).thenReturn(dto);
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
    void addMessageShouldReturnBadRequestOnServiceException() throws Exception {
        Long id = 0L;
        User user = User
                .builder()
                .id(id)
                .build();
        Message message = Message
                .builder()
                .content("message")
                .id(id)
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);
        when(this.principal.getUser()).thenReturn(user);
        when(this.modelMapper.map(dto, Message.class)).thenReturn(message);
        when(messageService.addMessage(any(Message.class))).thenThrow(MessageAdditionException.class);
        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void addMessageShouldReturnBadRequestOnContentBlank() throws Exception {
        Long id = 0L;
        Message message = Message
                .builder()
                .id(id)
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);

        mockMvc.perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }


    @Test
    void getAllMessagesForCurrentUserShouldWorkAsExpected() throws Exception {
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
    void getMessageByIdForCurrentUserShouldWorkAsExpected() throws Exception {
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
    void editMessageShouldWorkAsExpected() throws Exception {
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
    void editMessageShouldReturnBadRequestOnContentBlank() throws Exception {
        Long id = 0L;
        Message message = Message
                .builder()
                .content(" ")
                .id(id)
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);
        mockMvc.perform(
                put("/api/messages/0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void editMessageShouldReturnBadRequestOnExceptionThrownByService() throws Exception {
        Long id = 0L;
        Message message = Message
                .builder()
                .content("wrongUser")
                .id(id)
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);
        when(this.messageService.findById(any())).thenThrow(MessageRetrievalException.class);
        mockMvc.perform(
                put("/api/messages/0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void editMessageShouldReturnBadRequestOnWrongUser() throws Exception {
        Long id = 0L;
        User user = User.builder()
                .id(1L)
                .build();
        Message message = Message
                .builder()
                .user(user)
                .content("wrongUser")
                .id(id)
                .build();
        MessageDTO dto = this.realModelMapper.map(message, MessageDTO.class);
        String json = gson.toJson(dto);
        when(this.messageService.findById(any())).thenReturn(message);
        when(this.principal.getUserId()).thenReturn(2L);
        mockMvc.perform(
                put("/api/messages/0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deleteMessageShouldWorkAsExpected() throws Exception {
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
    @Test
    void deleteMessageShouldThrowOnWrongUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();
        Message toBeDeleted = Message.builder()
                .id(1L)
                .content("blabla")
                .user(user)
                .build();
        when(messageService.findById(any())).thenReturn(toBeDeleted);
        when(principal.getUserId()).thenReturn(2L);
        MessageDTO dto = this.realModelMapper.map(toBeDeleted, MessageDTO.class);
        String json = gson.toJson(dto);
        Assertions.assertTrue(!json.isEmpty());
        mockMvc.perform(
                delete("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteMessageShouldThrowServiceExceptionThrown() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();
        Message toBeDeleted = Message.builder()
                .id(1L)
                .content("blabla")
                .user(user)
                .build();
        when(messageService.findById(any())).thenReturn(toBeDeleted);
        when(principal.getUserId()).thenReturn(user.getId());
        doThrow(MessageDeletionException.class).when(messageService).deleteMessage(any());
        MessageDTO dto = this.realModelMapper.map(toBeDeleted, MessageDTO.class);
        String json = gson.toJson(dto);
        Assertions.assertTrue(!json.isEmpty());
        mockMvc.perform(
                delete("/api/messages/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
