package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;

@Service
public interface UserService extends UserDetailsService {
    User findByLogin(String login) throws UserRetrievalException;
    User findById(Long id) throws UserRetrievalException;
    User addUser(User user) throws UsernameAlreadyExistsException, UserAdditionException;
    void updateUser(User user) throws UserRetrievalException;
    User registerUser(User user) throws UsernameAlreadyExistsException, UserAdditionException;
}
