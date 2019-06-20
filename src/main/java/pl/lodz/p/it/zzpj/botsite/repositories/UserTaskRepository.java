package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserTaskRepository extends CrudRepository<UserTask, Long> {
    List<UserTask> findAllByUser(User user);
    @Query("select u from UserTask u where u.reminderDate >= :time and u.isDone = false")
    List<UserTask> findAllUnfinishedTasks(LocalDateTime time);
}
