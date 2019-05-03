package pl.lodz.p.it.zzpj.botsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserRole;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.util.Arrays;

@SpringBootApplication
public class BotSiteApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(BotSiteApplication.class, args);
    }

    @Override
    public void run(String... args) {
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
