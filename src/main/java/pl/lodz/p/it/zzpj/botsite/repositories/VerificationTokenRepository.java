package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationTokenInfo, Long> {
    Optional<VerificationTokenInfo> findByToken(String token);
}
