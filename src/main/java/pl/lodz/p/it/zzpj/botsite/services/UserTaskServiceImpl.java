package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskStatusException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
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
    public void addUserTask(UserTask task) throws UserTaskIdAlreadyExistsException {
        if (!this.userTaskRepository.findById(task.getId()).isPresent()) {
            this.userTaskRepository.save(task);
        } else {
            throw new UserTaskIdAlreadyExistsException("Task with that ID already exists");
        }
    }

    @Override
    public UserTask findById(String id) throws UserTaskRetrievalException {
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            return task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."));
        } catch (final Exception e) {
            throw new UserTaskRetrievalException("Could not retrieve task by ID.", e);
        }
    }

    //TODO Need to decide if it is necessary
    @Override
    public List<UserTask> getListOfUserTasksByUserId(String userId) throws UserTaskNotFoundException {
        return null;
    }

    //TODO Need to decide if it is necessary
    @Override
    public List<UserTask> getListOfUserTasksByBotId(String botId) throws UserTaskNotFoundException {
        return null;
    }

    @Override
    public void updateDate(String id, LocalDateTime newDateTime) throws UserTaskNotFoundException, DateTimeException {
        if (newDateTime.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("Cannot set reminder to this date");
        }
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            (task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."))).setReminderDate(newDateTime);
            this.userTaskRepository.save(task.get());
        } catch (final Exception e) {
            throw new UserTaskNotFoundException("Task with that ID not found.", e);
        }
    }

    @Override
    public void updateIsRepeatableStatus(String id, boolean status) throws UserTaskStatusException {
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            (task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."))).setRepeatable(status);
            this.userTaskRepository.save(task.get());
        } catch (final Exception e) {
            throw new UserTaskStatusException(String.format("Task for %s cannot be updated", id), e);
        }
    }

    @Override
    public void updateIsDoneStatus(String id, boolean status) throws UserTaskStatusException{
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(id);
            (task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."))).setDone(status);
            this.userTaskRepository.save(task.get());
        } catch (final Exception e) {
            throw new UserTaskStatusException(String.format("Task for %s cannot be updated", id), e);
        }
    }

    //TODO Message implementation needed
    @Override
    public void sendMessage(String botId, String messageId) {

    }
}
