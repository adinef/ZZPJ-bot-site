package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskStatusException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserTaskService {
    void addUserTask(UserTask task) throws UserTaskIdAlreadyExistsException;
    UserTask findById(String id) throws UserTaskRetrievalException;
    List<UserTask> getListOfUserTasksByUserId(String userId) throws UserTaskNotFoundException;
    List<UserTask> getListOfUserTasksByBotId(String botId) throws UserTaskNotFoundException;
    void updateDate(String id, LocalDateTime newDateTime) throws UserTaskNotFoundException, DateTimeException;
    void updateIsRepeatableStatus(String id, boolean status) throws UserTaskStatusException;
    void updateIsDoneStatus(String id, boolean status) throws UserTaskStatusException;
    void sendMessage(String botId, String messageId);
}
