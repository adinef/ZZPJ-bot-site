package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;

import java.util.Optional;

@Repository
public interface BotRepository extends MongoRepository<Bot, String> {
}