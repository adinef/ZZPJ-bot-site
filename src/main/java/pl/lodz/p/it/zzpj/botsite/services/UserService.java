package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.VerificationTokenInfoNotFoundException;

public interface UserService extends UserDetailsService {
    User findByLogin(String login) throws UserRetrievalException;
    void addUser(User user) throws UsernameAlreadyExistsException;
    void saveToken(User user, String token);
    VerificationTokenInfo findVerificationTokenInfo(String token) throws VerificationTokenInfoNotFoundException;
}
