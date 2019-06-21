package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.repositories.MessageRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        this.messageService = new MessageServiceImpl(messageRepository, userRepository);
    }

    @Test
    void findByIdShouldReturnEntityOfMessage() throws MessageRetrievalException {
        User user = User
                .builder()
                .id(1L)
                .build();
        Message message = Message
                .builder()
                .id(1L)
                .user(user)
                .content("content")
                .build();
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        Assertions.assertEquals((Long) 1L, messageService.findById(1L).getId());
    }

    @Test
    void findByIdShouldThrowExceptionAfterDatabaseRuntimeException() {
        when(messageRepository.findById(any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(MessageRetrievalException.class, () -> messageService.findById(1L));
    }

    @Test
    void findByIdShouldThrowExceptionWhenMessageNotFound() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(MessageRetrievalException.class, () -> messageService.findById(1L));
    }

    @Test
    void addMessageShouldAddMessageToDatabase() throws MessageAdditionException, UserRetrievalException {
        User user = User
                .builder()
                .id(1L)
                .build();
        Message message = Message
                .builder()
                .id(1L)
                .user(user)
                .content("content")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        messageService.addMessage(1L, message);
        verify(messageRepository).save(message);
    }

    @Test
    void addMessageShouldThrowMessageAdditionException() {
        Message message = Message
                .builder()
                .build();
        Assertions.assertThrows(MessageAdditionException.class, () -> messageService.addMessage(1L,message));
    }

    @Test
    void updateShouldProceedWithUpdate() throws MessageNotFoundException, MessageUpdateException {
        User user = User
                .builder()
                .id(1L)
                .build();
        Message message = Message
                .builder()
                .id(1L)
                .user(user)
                .content("content")
                .build();
        when(messageRepository.findById(message.getId())).thenReturn(Optional.of(message));
        when(messageRepository.save(message)).thenReturn(message);
        Assertions.assertEquals(message, messageService.updateMessage(1L, 1L, "contents"));
    }

    @Test
    void updateShouldThrowWhenMessageByIdNotFound() {
        Message message = Message
                .builder()
                .id(1L)
                .content("content")
                .build();
        when(messageRepository.findById(message.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(MessageNotFoundException.class, () -> messageService.updateMessage(null, 1L, "contents"));
    }

    @Test
    void updateContentBlankShouldThrowUpdateException() {
        Message message = Message
                .builder()
                .id(1L)
                .content("content")
                .build();
        Assertions.assertThrows(MessageUpdateException.class, () -> messageService.updateMessage(null, 1L, " "));
    }
}
