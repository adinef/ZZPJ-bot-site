package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskStatusException;

import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

public interface UserTaskService {
    void addUserTask(UserTask task) throws UserTaskIdAlreadyExistsException;
    UserTask findById(String userTaskId) throws UserTaskRetrievalException;
    List<UserTask> findListByUserId(String userId) throws UserTaskNotFoundException;
    List<UserTask> findListByBotId(String botId) throws UserTaskNotFoundException;
    void updateDate(String userTaskId, Date newDate) throws UserTaskNotFoundException, DateTimeException;
    void updateIsRepeatableStatus(String userTaskId, boolean status) throws UserTaskStatusException;
    void updateIsDoneStatus(String userTaskId, boolean status) throws UserTaskStatusException;
    void sendMessage(String botId, String messageId);
}
