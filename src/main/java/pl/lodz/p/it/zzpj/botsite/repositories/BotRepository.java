package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;

@Repository
public interface BotRepository extends CrudRepository<Bot, Long> {
}