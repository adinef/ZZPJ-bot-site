package pl.lodz.p.it.zzpj.botsite.tasking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.zzpj.botsite.botservices.BotMessengerFactory;
import pl.lodz.p.it.zzpj.botsite.botservices.messengers.BotMessenger;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.services.MessengerDoesntExistException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserTaskRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
public class UserTaskPooler {

    private final UserTaskRepository userTaskRepository;
    private final BotMessengerFactory messengerFactory;

    @Autowired
    public UserTaskPooler(
            UserTaskRepository userTaskRepository,
            BotMessengerFactory messengerFactory) {
        this.userTaskRepository = userTaskRepository;
        this.messengerFactory = messengerFactory;
    }

    @Scheduled(fixedRate = 60000)
    public void goThroughTasks() {
        userTaskRepository
                .findAllUnfinishedTasks(LocalDateTime.now())
                .parallelStream()
                .forEach(userTask -> {
                            try {
                                BotMessenger messenger = this.messengerFactory.getForBot(userTask.getBot());
                                messenger.sendMessage(userTask.getMessage());
                            } catch (MessengerDoesntExistException ex) {
                                log.error("Error sending message " + userTask.getUser().getLogin() + ":" +
                                        userTask.getMessage() + "\n" + ex.getLocalizedMessage());
                                ex.printStackTrace();
                            }
                            if (userTask.isRepeatable()) {
                                Duration duration = Duration
                                        .between(userTask.getCreationDate(), userTask.getReminderDate());
                                userTask.setReminderDate(
                                        userTask.getReminderDate().plus(duration)
                                );
                            } else {
                                userTask.setDone(true);
                            }
                            this.userTaskRepository.save(userTask);
                        }
                );
    }
}
