package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
