package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.UserTaskDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;

import java.util.List;

public interface UserTaskService {
    UserTask addUserTask(UserTask task) throws UserTaskIdAlreadyExistsException, UserTaskAdditionException;
    UserTask findById(Long id) throws UserTaskRetrievalException;
    List<UserTask> getListOfUserTasksByUserId(Long userId) throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException;
    UserTask update(UserTask userTask) throws UserTaskUpdateException;
    void deleteUserTask(Long id) throws UserTaskDeletionException;
}
