package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.UserTaskDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("userTaskService")
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserTaskServiceImpl(UserTaskRepository userTaskRepository, UserRepository userRepository) {
        this.userTaskRepository = userTaskRepository;
        this.userRepository = userRepository;
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

    @Override
    public List<UserTask> getListOfUserTasksByUserId(Long userId) throws UserNotFoundException, UserTaskUpdateException {
        try {
            User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            return this.userTaskRepository.findAllByUser(user);
        } catch (final RuntimeException e) {
            throw new UserTaskUpdateException("Task could not be updated.", e);
        }
    }

    @Override
    public UserTask update(UserTask userTask) throws UserTaskUpdateException {
        try {
            return this.userTaskRepository.save(userTask);
        } catch (final Exception e) {
            throw new UserTaskUpdateException("User task could not be updated.", e);
        }
    }

    @Override
    public void deleteUserTask(Long id) throws UserTaskDeletionException {
        try {
            userTaskRepository.deleteById(id);
        } catch (final Exception e) {
            throw new UserTaskDeletionException("Could not delete task.", e);
        }
    }
}
