package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.MyUserDetails;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.util.Optional;

@Service("mongoUserService")
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByLogin(String login) throws UserRetrievalException {
        try {
            Optional<User> user = this.userRepository.findById(login);
            return user.orElseThrow(() -> new UserNotFoundException("User with that login not found."));
        } catch (final Exception e) {
            throw new UserRetrievalException("Could not retrieve user by login", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new MyUserDetails(this.findByLogin(username));
        } catch (final Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
