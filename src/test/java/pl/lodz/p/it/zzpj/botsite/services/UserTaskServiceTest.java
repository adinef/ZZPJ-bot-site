package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.UserTaskDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void updateShouldProceedWithUpdate() throws UserTaskUpdateException {
        UserTask task = UserTask
                .builder()
                .id(1L)
                .reminderDate(LocalDateTime.now().plusDays(1))
                .build();
        when(userTaskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userTaskRepository.save(task)).thenReturn(task);
        Assertions.assertEquals(task, userTaskService.update(task));
    }

    @Test
    void updateShouldThrowWhenTaskByIdNotFound() {
        UserTask task = UserTask
                .builder()
                .id(1L)
                .reminderDate(LocalDateTime.now().plusDays(1))
                .build();
        when(userTaskRepository.findById(task.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserTaskUpdateException.class, () -> userTaskService.update(task));
    }

    @Test
    void updateDateShouldThrowDateTimeException() {
        LocalDateTime today = LocalDateTime.now();
        UserTask task = UserTask
                .builder()
                .id(1L)
                .reminderDate(today.minusDays(1))
                .build();
        Assertions.assertThrows(DateTimeException.class, () -> userTaskService.update(task));
    }

    @Test
    void deleteUserTaskShouldDeleteTask() throws UserTaskDeletionException {
        LocalDateTime today = LocalDateTime.now();
        UserTask task = UserTask
                .builder()
                .id(1L)
                .reminderDate(today)
                .isRepeatable(true)
                .build();
        userTaskService.deleteUserTask(task.getId());
        verify(userTaskRepository).deleteById(task.getId());
    }
}
