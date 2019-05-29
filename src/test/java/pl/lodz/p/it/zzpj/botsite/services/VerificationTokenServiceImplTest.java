package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.VerificationTokenInfo;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.VerificationTokenInfoNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.VerificationTokenRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class VerificationTokenServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @BeforeEach
    public void setup() {

        UserService userService = new UserServiceImpl(userRepository, passwordEncoder);

        this.verificationTokenService = new VerificationTokenServiceImpl(
                userService, this.verificationTokenRepository
        );
    }

    @Test
    void saveTokenShouldWorkAsExpected() throws UserRetrievalException {

        String token = UUID.randomUUID().toString();

        User user = User
                .builder()
                .login("ValidLogin")
                .password("somePassword")
                .email("ValidEmail@hmail.com")
                .build();

        VerificationTokenInfo tokenInfo = new VerificationTokenInfo(user, token);

        when(userRepository.findById(user.getLogin())).thenReturn(Optional.of(user));

        this.verificationTokenService.saveToken(user, token);

        verify(verificationTokenRepository).save(tokenInfo);
    }

    @Test
    void saveTokenShouldThrowExceptionWhenUserNotFound() {
        String token = UUID.randomUUID().toString();

        User user = User
                .builder()
                .login("ValidLogin")
                .password("somePassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findById(user.getLogin())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserRetrievalException.class,
                () -> verificationTokenService.saveToken(user, token));

    }

    @Test
    void findVerificationTokenInfoShouldWorkAsExpected() throws VerificationTokenInfoNotFoundException {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        String token = UUID.randomUUID().toString();

        when(verificationTokenRepository.findByToken(token)).thenReturn(
                Optional.of(new VerificationTokenInfo(user, token)));

        verificationTokenService.findVerificationTokenInfo(token);

        verify(verificationTokenRepository).findByToken(token);
    }

    @Test
    void findVerificationTokenInfoShouldThrowExceptionIfTokenNotFound() {

        String token = UUID.randomUUID().toString();

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Assertions.assertThrows(VerificationTokenInfoNotFoundException.class,
                () -> verificationTokenService.findVerificationTokenInfo(token));
    }
}
