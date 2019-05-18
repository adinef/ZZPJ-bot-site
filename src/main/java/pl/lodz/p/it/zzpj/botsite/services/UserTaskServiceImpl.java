package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskStatusException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.DateTimeException;
import java.util.Date;
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
        if (!this.userTaskRepository.findById(task.getUserTaskId()).isPresent()) {
            this.userTaskRepository.save(task);
        } else {
            throw new UserTaskIdAlreadyExistsException("Task with that ID already exists");
        }
    }

    @Override
    public UserTask findById(String userTaskId) throws UserTaskRetrievalException {
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(userTaskId);
            return task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."));
        } catch (final Exception e) {
            throw new UserTaskRetrievalException("Could not retrieve task by ID.", e);
        }
    }

    //TODO Need to decide if it is necessary
    @Override
    public List<UserTask> findListByUserId(String userId) throws UserTaskNotFoundException {
        throw new NotImplementedException();
    }

    //TODO Need to decide if it is necessary
    @Override
    public List<UserTask> findListByBotId(String botId) throws UserTaskNotFoundException {
        throw new NotImplementedException();
    }

    @Override
    public void updateDate(String userTaskId, Date newDate) throws UserTaskNotFoundException, DateTimeException {
        if (newDate.before(new Date())) {
            throw new DateTimeException("Cannot set reminder to this date");
        }
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(userTaskId);
            (task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."))).setReminderDate(newDate);
            this.userTaskRepository.save(task.get());
        } catch (final Exception e) {
            throw new UserTaskNotFoundException("Task with that ID not found.", e);
        }
    }

    @Override
    public void updateIsRepeatableStatus(String userTaskId, boolean status) throws UserTaskStatusException {
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(userTaskId);
            (task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."))).setRepeatable(status);
            this.userTaskRepository.save(task.get());
        } catch (final Exception e) {
            throw new UserTaskStatusException(String.format("Task for %s cannot be updated", userTaskId), e);
        }
    }

    @Override
    public void updateIsDoneStatus(String userTaskId, boolean status) throws UserTaskStatusException{
        try {
            Optional<UserTask> task = this.userTaskRepository.findById(userTaskId);
            (task.orElseThrow(() -> new UserTaskNotFoundException("Task with that ID not found."))).setDone(status);
            this.userTaskRepository.save(task.get());
        } catch (final Exception e) {
            throw new UserTaskStatusException(String.format("Task for %s cannot be updated", userTaskId), e);
        }
    }

    //TODO Message implementation needed
    @Override
    public void sendMessage(String botId, String messageId) {
        throw new NotImplementedException();
    }
}
