package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserRetrievalException;

public interface UserService extends UserDetailsService {
    User findByLogin(String login) throws UserRetrievalException;
    void addUser(User user);
}
