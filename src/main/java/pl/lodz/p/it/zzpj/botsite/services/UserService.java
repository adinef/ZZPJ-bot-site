package pl.lodz.p.it.zzpj.botsite.services;

import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserRetrievalException;

public interface UserService {
    User findByLogin(String login) throws UserRetrievalException;
}
