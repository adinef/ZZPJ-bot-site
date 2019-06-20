package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;

import java.util.List;

@Repository
public interface UserTaskRepository extends CrudRepository<UserTask, Long> {
    List<UserTask> findAllByUser(User user);
}
