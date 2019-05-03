package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
