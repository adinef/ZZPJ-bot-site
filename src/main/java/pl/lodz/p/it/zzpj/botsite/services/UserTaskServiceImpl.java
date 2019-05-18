package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserTaskRetrievalException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

public class UserTaskServiceImpl implements UserTaskService {

    @Override
    public void addTask() throws UserTaskIdAlreadyExistsException {

    }

    @Override
    public UserTask findById(String userTaskId) throws UserTaskRetrievalException {
        return null;
    }

    @Override
    public List<UserTask> findListByUserId(String userId) throws UserTaskNotFoundException {
        return null;
    }

    @Override
    public List<UserTask> findListByBotId(String botId) throws UserTaskNotFoundException {
        return null;
    }

    @Override
    public void updateDate(String userTaskId, Date newDate) throws UserTaskNotFoundException, DateTimeException {

    }

    @Override
    public void updateIsRepeatableStatus(String userTaskId, boolean status) {

    }

    @Override
    public void updateIsDoneStatus(String userTaskId, boolean status) {

    }

    @Override
    public void sendMessage(String botId, String messageId) {
        throw new NotImplementedException();
    }
}
