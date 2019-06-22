package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserRole;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.web.dto.MyUserDetails;

import java.util.Arrays;
import java.util.Optional;

@Service("mongoUserService")

public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByLogin(String login) throws UserRetrievalException {
        try {
            Optional<User> user = this.userRepository.findByLogin(login);
            return user.orElseThrow(() -> new UserNotFoundException("User with that login not found."));
        } catch (final Exception e) {
            throw new UserRetrievalException("Could not retrieve user by login", e);
        }
    }


    @Override
    public User addUser(User user) throws UsernameAlreadyExistsException, UserAdditionException {
        if (!this.userRepository.findByLogin(user.getLogin()).isPresent()) {
            try {
                user.setPassword(
                        this.passwordEncoder.encode(user.getPassword())
                );
                return this.userRepository.save(user);
            } catch (final Exception e) {
                throw new UserAdditionException("Could not add user.", e);
            }
        } else {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return MyUserDetails.of(this.findByLogin(username));
        } catch (final Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) throws UserRetrievalException, UserUpdateException {
        this.findByLogin(user.getLogin());

        Optional<User> emailHolder = this.userRepository.findByEmail(user.getEmail());

        boolean userWithGivenEmailExists = emailHolder.isPresent();

        if (userWithGivenEmailExists) {

            boolean principalIsEmailHolder = emailHolder.get().getLogin().equals(user.getLogin());

            if (!principalIsEmailHolder) {
                throw new UserUpdateException("User with given email already exists!");
            }

        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        this.userRepository.save(user);
    }

    @Override
    public User registerUser(User user) throws UsernameAlreadyExistsException, UserAdditionException {
        if (!this.userRepository.findByLogin(user.getLogin()).isPresent()) {
            try {
                user.setPassword(
                        this.passwordEncoder.encode(user.getPassword())
                );
                user.setRoles(Arrays.asList(UserRole.USER));
                return this.userRepository.save(user);
            } catch (final Exception e) {
                throw new UserAdditionException("Could not add user.", e);
            }
        } else {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
    }

}
