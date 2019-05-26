
package pl.lodz.p.it.zzpj.botsite.services;

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
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.VerificationTokenRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @BeforeEach
    public void setup() {
        this.userService = new UserServiceImpl(userRepository, passwordEncoder, verificationTokenRepository);
    }

    @Test
    void findByLoginShouldReturnEntityWhenExpected() throws UserRetrievalException {
        User user = User
                .builder()
                .login("test")
                .build();
        when(userRepository.findById("test")).thenReturn(Optional.of(user));
        Assertions.assertEquals("test", userService.findByLogin("test").getLogin());
    }

    @Test
    void findByLoginShouldThrowRightExceptionAfterDatabaseRuntimeException() {
        when(userRepository.findById(anyString())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(UserRetrievalException.class, () -> userService.findByLogin("test"));
    }

    @Test
    void findByLoginShouldThrowRightExceptionWhenUserEmpty() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserRetrievalException.class, () -> userService.findByLogin("test"));
    }

    @Test
    void loadUserByUsernameShouldReturnEntityWhenExpected() {
        User user = User
                .builder()
                .login("test")
                .build();
        when(userRepository.findById("test")).thenReturn(Optional.of(user));
        Assertions.assertEquals("test", userService.loadUserByUsername("test").getUsername());
    }

    @Test
    void loadUserByUsernameShouldReturnThrowsRightExceptionWhenUserNotFound() {
        when(userRepository.findById("test")).thenReturn(Optional.empty());
        Assertions.assertThrows(UserRetrievalException.class, () -> userService.findByLogin("test"));
    }

    @Test
    void addUserShouldAddValidUser() throws UsernameAlreadyExistsException, UserAdditionException {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        userService.addUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void addUserShouldThrowUserAlreadyExistsExceptionWhenExpected() {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository
                .findById(user.getLogin()))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(UsernameAlreadyExistsException.class,
                () -> userService.addUser(user));
    }

    @Test
    void addUserShouldSaveUserWithHashedPassword() throws UsernameAlreadyExistsException, UserAdditionException {

        String usersPassword = "ExtremelyPowerfulLongPassword";
        String mockForHashedPass = "AWESOME_HASH";

        User userWithPlainTextPassword = User
                .builder()
                .login("ValidLogin")
                .password(usersPassword)
                .email("ValidEmail@hmail.com")
                .build();

        User userWithHashedPassword = User
                .builder()
                .login("ValidLogin")
                .password(mockForHashedPass)
                .email("ValidEmail@hmail.com")
                .build();

        when(this.passwordEncoder.encode(anyString())).thenReturn(mockForHashedPass);

        userService.addUser(userWithPlainTextPassword);

        verify(userRepository).save(userWithHashedPassword);
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

        this.userService.saveToken(user, token);

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
                () -> userService.saveToken(user, token));

    }

    @Test
    void updateUserShouldWorkAsExpected() throws UserRetrievalException {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findById(user.getLogin())).thenReturn(Optional.of(user));

        userService.updateUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserThrowExceptionWhenUserNotFound() {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findById(user.getLogin())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserRetrievalException.class, () -> userService.updateUser(user));
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

        userService.findVerificationTokenInfo(token);

        verify(verificationTokenRepository).findByToken(token);
    }

    @Test
    void findVerificationTokenInfoShouldThrowExceptionIfTokenNotFound() {

        String token = UUID.randomUUID().toString();

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Assertions.assertThrows(VerificationTokenInfoNotFoundException.class,
                () -> userService.findVerificationTokenInfo(token));
    }


}