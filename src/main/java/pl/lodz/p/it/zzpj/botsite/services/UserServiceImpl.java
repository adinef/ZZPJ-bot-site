package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.exceptions.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.VerificationTokenRepository;
import pl.lodz.p.it.zzpj.botsite.web.dto.MyUserDetails;

import java.util.Optional;
import java.util.UUID;

@Service("mongoUserService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            VerificationTokenRepository verificationTokenRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public User findByLogin(String login) throws UserRetrievalException {
        try {
            Optional<User> user = this.userRepository.findById(login);
            return user.orElseThrow(() -> new UserNotFoundException("User with that login not found."));
        } catch (final Exception e) {
            throw new UserRetrievalException("Could not retrieve user by login", e);
        }
    }

    @Override
    public void addUser(User user) throws UsernameAlreadyExistsException {
        if (!this.userRepository.findById(user.getLogin()).isPresent()) {
            user.setPassword(
                    this.passwordEncoder.encode(user.getPassword())
            );
            this.userRepository.save(user);
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
    public void saveToken(User user, String token) {
        this.verificationTokenRepository.save(new VerificationTokenInfo(
                user,
                token
        ));
    }

    @Override
    public VerificationTokenInfo findVerificationTokenInfo(String token) throws VerificationTokenInfoNotFoundException {
        return this.verificationTokenRepository.findByToken(token).orElseThrow(() -> new VerificationTokenInfoNotFoundException());
    }
}
