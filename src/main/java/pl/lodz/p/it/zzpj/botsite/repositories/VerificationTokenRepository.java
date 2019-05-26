package pl.lodz.p.it.zzpj.botsite.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationTokenInfo, String> {
    Optional<VerificationTokenInfo> findByToken(String token);
}
