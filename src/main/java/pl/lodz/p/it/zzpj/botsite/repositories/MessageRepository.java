package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.lodz.p.it.zzpj.botsite.entities.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
