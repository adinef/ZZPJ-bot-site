package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotRepository extends CrudRepository<Bot, Long> {
    List<Bot> findAllByUser(User user);
    List<Bot> findAllByName(String name);
}