package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;

@Repository
public interface UserTaskRepository extends MongoRepository<UserTask, String> {
}
