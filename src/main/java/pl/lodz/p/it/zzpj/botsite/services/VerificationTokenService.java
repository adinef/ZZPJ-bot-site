package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;

public interface VerificationTokenService {
    VerificationTokenInfo findVerificationTokenInfo(String token) throws VerificationTokenInfoNotFoundException;
    void saveToken(User user, String token) throws UserRetrievalException;
}
