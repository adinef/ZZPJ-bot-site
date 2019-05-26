package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskStatusException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserTaskService {
    UserTask addUserTask(UserTask task) throws UserTaskIdAlreadyExistsException, UserTaskAdditionException;
    UserTask findById(String id) throws UserTaskRetrievalException;
    List<UserTask> getListOfUserTasksByUserId(String userId) throws UserTaskNotFoundException;
    List<UserTask> getListOfUserTasksByBotId(String botId) throws UserTaskNotFoundException;
    UserTask updateDate(String id, LocalDateTime newDateTime) throws UserTaskNotFoundException, DateTimeException, UserTaskUpdateException;
    UserTask updateIsRepeatableStatus(String id, boolean status) throws UserTaskStatusException;
    UserTask updateIsDoneStatus(String id, boolean status) throws UserTaskStatusException;
    void sendMessage(String botId, String messageId);
}
