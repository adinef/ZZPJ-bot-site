package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("mongoUserTaskService")
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskRepository userTaskRepository;

    @Autowired
    public UserTaskServiceImpl(UserTaskRepository userTaskRepository) {
        this.userTaskRepository = userTaskRepository;
    }

    @Override
    public UserTask findById(Long id) throws UserTaskRetrievalException {
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            return task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."));
        } catch (final Exception e) {
            throw new UserTaskRetrievalException("Could not retrieve task by ID.", e);
        }
    }

    @Override
    public UserTask addUserTask(UserTask task) throws UserTaskAdditionException {
        if (task.getId() != null) {
            throw new UserTaskAdditionException("User task id must be null on addition.");
        }
        try {
            return this.userTaskRepository.save(task);
        } catch (final Exception e) {
            throw new UserTaskAdditionException("Could not add user task", e);
        }
    }

    //TODO Need to decide if it is necessary
    @Override
    public List<UserTask> getListOfUserTasksByUserId(String userId) throws UserTaskNotFoundException {
        return new ArrayList<>();
    }

    //TODO Need to decide if it is necessary
    @Override
    public List<UserTask> getListOfUserTasksByBotId(Long botId) throws UserTaskNotFoundException {
        return null;
    }

    @Override
    public UserTask updateDate(Long id, LocalDateTime newDateTime) throws DateTimeException, UserTaskUpdateException {
        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("Cannot set reminder to this date");
        }
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            UserTask userTask = task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."));
            userTask.setReminderDate(newDateTime);
            return this.userTaskRepository.save(userTask);
        } catch (final Exception e) {
            throw new UserTaskUpdateException("Task could not be updated.", e);
        }
    }

    @Override
    public UserTask updateIsRepeatableStatus(Long id, boolean status) throws UserTaskStatusException {
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            UserTask userTask = task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."));
            userTask.setRepeatable(status);
            return this.userTaskRepository.save(userTask);
        } catch (final Exception e) {
            throw new UserTaskStatusException(String.format("Task for %s cannot be updated", id), e);
        }
    }

    @Override
    public UserTask updateIsDoneStatus(Long id, boolean status) throws UserTaskStatusException{
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            UserTask userTask = task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."));
            userTask.setDone(status);
            return this.userTaskRepository.save(userTask);
        } catch (final Exception e) {
            throw new UserTaskStatusException(String.format("Task for %s cannot be updated", id), e);
        }
    }

    //TODO Message implementation needed
    @Override
    public void sendMessage(String botId, String messageId) {

    }
}
