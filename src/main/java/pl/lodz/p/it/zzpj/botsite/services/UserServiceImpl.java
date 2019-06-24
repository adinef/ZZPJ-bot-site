package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service("userService")

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
    public User findById(Long id) throws UserRetrievalException {
        try {
            Optional<User> user = this.userRepository.findById(id);
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
    public void updateUser(User newUserData, Long userId) throws UserRetrievalException, UserUpdateException {
        User oldUser = this.userRepository.findById(userId).orElseThrow(UserRetrievalException::new);

        boolean emailProvided = newUserData.getEmail() != null;
        boolean passwordProvided = newUserData.getPassword() != null;
        boolean nameProvided = newUserData.getName() != null;
        boolean lastNameProvided = newUserData.getLastName() != null;

        if (emailProvided) {
            Optional<User> emailHolder = this.userRepository.findByEmail(newUserData.getEmail());

            boolean userWithGivenEmailExists = emailHolder.isPresent();
            if (userWithGivenEmailExists && !emailHolder.get().equals(oldUser)) {
                throw new UserUpdateException("User with given email already exists!");
            }
        }

        if (passwordProvided) {
            newUserData.setPassword(passwordEncoder.encode(newUserData.getPassword()));
        }

        User userToSave = User
                .builder()
                .id(userId)
                .login(oldUser.getLogin())
                .password(passwordProvided ? newUserData.getPassword() : oldUser.getPassword())
                .name(nameProvided ? newUserData.getName() : oldUser.getName())
                .lastName(lastNameProvided ? newUserData.getLastName() : oldUser.getLastName())
                .email(emailProvided ? newUserData.getEmail() : oldUser.getEmail())
                .active(oldUser.isActive())
                .build();


        this.userRepository.save(userToSave);
    }

    @Override
    @Transactional
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
