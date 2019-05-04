package pl.lodz.p.it.zzpj.botsite.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserRole;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@Profile("init")
@Slf4j
public class AdminInit {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        log.info("Creating default admin user...");
        User user = User
                .builder()
                .login("admin")
                .active(true)
                .roles(Arrays.asList( UserRole.ADMIN ) )
                .build();
        Example<User> userFilter = Example.of(user);
        if (!userRepository.exists(userFilter)) {
            user.setPassword(
                    passwordEncoder.encode("admin")
            );
            userRepository.insert(user);
        }
    }
}
