package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskStatusException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTaskServiceTest {

    @Autowired
    private UserTaskService userTaskService;

    @Mock
    private UserTaskRepository userTaskRepository;


    @BeforeEach
    void setup() {
        this.userTaskService = new UserTaskServiceImpl(userTaskRepository);
    }

    @Test
    void findByIdShouldReturnEntityOfUserTask() throws UserTaskRetrievalException {
        UserTask task = UserTask
                .builder()
                .id("id")
                .build();
        when(userTaskRepository.findById("id")).thenReturn(Optional.of(task));
        Assertions.assertEquals("id", userTaskService.findById("id").getId());
    }

    @Test
    void findByIdShouldThrowExceptionAfterDatabaseRuntimeException() {
        when(userTaskRepository.findById(anyString())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(UserTaskRetrievalException.class, () -> userTaskService.findById("id"));
    }

    @Test
    void findByIdShouldThrowExceptionWhenTaskNotFound() {
        when(userTaskRepository.findById("id")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserTaskRetrievalException.class, () -> userTaskService.findById("id"));
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
                .id("id")
                .build();
        Assertions.assertThrows(UserTaskAdditionException.class, () -> userTaskService.addUserTask(task));
    }

    @Test
    void updateDateShouldChangeTaskDate() throws DateTimeException, UserTaskNotFoundException, UserTaskIdAlreadyExistsException, UserTaskUpdateException {
        LocalDateTime today = LocalDateTime.now();
        UserTask task = UserTask
                .builder()
                .id("id")
                .reminderDate(today)
                .build();
        when(userTaskRepository
                .findById(task.getId())
        ).thenReturn(Optional.of(task));
        userTaskService.updateDate(task.getId(), today.plusDays(1));
        Assertions.assertEquals(today.plusDays(1), task.getReminderDate());
    }

    @Test
    void updateDateShouldThrowDateTimeException() {
        LocalDateTime today = LocalDateTime.now();
        UserTask task = UserTask
                .builder()
                .id("id")
                .reminderDate(today)
                .build();
        Assertions.assertThrows(DateTimeException.class, () -> userTaskService.updateDate(task.getId(), today.minusDays(1)));
    }

    @Test
    void updateIsRepeatableStatusShouldUpdateTaskStatus() throws UserTaskStatusException {
        UserTask task = UserTask
                .builder()
                .id("id")
                .isRepeatable(true)
                .build();
        when(userTaskRepository
                .findById(task.getId())
        ).thenReturn(Optional.of(task));
        userTaskService.updateIsRepeatableStatus(task.getId(), false);
        Assertions.assertFalse(task.isRepeatable());
    }

    @Test
    void updateIsDoneStatusShouldUpdateTaskStatus() throws UserTaskStatusException {
        UserTask task = UserTask
                .builder()
                .id("id")
                .isDone(false)
                .build();
        when(userTaskRepository
                .findById(task.getId())
        ).thenReturn(Optional.of(task));
        userTaskService.updateIsDoneStatus(task.getId(), true);
        Assertions.assertTrue(task.isDone());
    }
}
