package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskStatusException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTaskServiceTest {

    @Autowired
    private UserTaskService userTaskService;

    @Mock
    private UserTaskRepository userTaskRepository;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        this.userTaskService = new UserTaskServiceImpl(userTaskRepository, userRepository);
    }

    @Test
    void findByIdShouldReturnEntityOfUserTask() throws UserTaskRetrievalException {
        UserTask task = UserTask
                .builder()
                .id(1L)
                .build();
        when(userTaskRepository.findById(1L)).thenReturn(Optional.of(task));
        Assertions.assertEquals((Long)1L, userTaskService.findById(1L).getId());
    }

    @Test
    void findByIdShouldThrowExceptionAfterDatabaseRuntimeException() {
        when(userTaskRepository.findById(any())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(UserTaskRetrievalException.class, () -> userTaskService.findById(1L));
    }

    @Test
    void findByIdShouldThrowExceptionWhenTaskNotFound() {
        when(userTaskRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserTaskRetrievalException.class, () -> userTaskService.findById(1L));
    }

    @Test
    void addUserTaskShouldAddTaskToDatabase() throws UserTaskIdAlreadyExistsException, UserTaskAdditionException {
        UserTask task = UserTask
                .builder()
                .build();
        userTaskService.addUserTask(task);
        verify(userTaskRepository).save(task);
    }

    @Test
    void addUserTaskShouldThrowUserTaskAdditionException() {
        UserTask task = UserTask
                .builder()
                .id(1L)
                .build();
        Assertions.assertThrows(UserTaskAdditionException.class, () -> userTaskService.addUserTask(task));
    }
}
