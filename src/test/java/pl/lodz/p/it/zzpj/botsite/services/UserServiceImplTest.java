
package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UsernameAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.VerificationTokenRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
        this.userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void findByLoginShouldReturnEntityWhenExpected() throws UserRetrievalException {
        User user = User
                .builder()
                .login("test")
                .build();
        when(userRepository.findByLogin("test")).thenReturn(Optional.of(user));
        Assertions.assertEquals("test", userService.findByLogin("test").getLogin());
    }

    @Test
    void findByLoginShouldThrowRightExceptionAfterDatabaseRuntimeException() {
        when(userRepository.findByLogin(anyString())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(UserRetrievalException.class, () -> userService.findByLogin("test"));
    }

    @Test
    void findByLoginShouldThrowRightExceptionWhenUserEmpty() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserRetrievalException.class, () -> userService.findByLogin("test"));
    }

    @Test
    void loadUserByUsernameShouldReturnEntityWhenExpected() {
        User user = User
                .builder()
                .login("test")
                .build();
        when(userRepository.findByLogin("test")).thenReturn(Optional.of(user));
        Assertions.assertEquals("test", userService.loadUserByUsername("test").getUsername());
    }

    @Test
    void loadUserByUsernameShouldReturnThrowsRightExceptionWhenUserNotFound() {
        when(userRepository.findByLogin("test")).thenReturn(Optional.empty());
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
                .findByLogin(user.getLogin()))
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
    void updateUserShouldWorkAsExpected() throws UserRetrievalException, UserUpdateException {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        userService.updateUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldThrowExceptionWhenUserNotFound() {
        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserRetrievalException.class, () -> userService.updateUser(user));
    }

    @Test
    void updateUserShouldThrowExceptionWhenOtherUserWithGivenEmailAlreadyExists() {


        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        User otherUser = User
                .builder()
                .login("OtherLogin")
                .password("Pass")
                .email("otherEmail@hmail.com")
                .build();

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(otherUser));

        Assertions.assertThrows(UserUpdateException.class, () -> userService.updateUser(user));


    }

    @Test
    void updateUserShouldNotThrowExceptionWhenEmailIsTheSame() {


        User user = User
                .builder()
                .login("ValidLogin")
                .password("ValidPassword")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.updateUser(user));


    }

    @Test
    void updateUserShouldHashPassword() throws UserRetrievalException, UserUpdateException {

        String password = "ValidPassword";
        String hashedPassword = "HASH";

        User user = User
                .builder()
                .login("ValidLogin")
                .password(password)
                .email("ValidEmail@hmail.com")
                .build();

        User userWithHashedPwd = User
                .builder()
                .login(user.getLogin())
                .password(hashedPassword)
                .email(user.getEmail())
                .build();

        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        userService.updateUser(user);

        verify(passwordEncoder).encode(password);
        verify(userRepository).save(userWithHashedPwd);


    }

    @Test
    void updateUserShouldNotUpdatePasswordIfItIsNull() throws UserRetrievalException, UserUpdateException {
        User user = User
                .builder()
                .login("ValidLogin")
                .email("ValidEmail@hmail.com")
                .build();

        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        userService.updateUser(user);
        verify(userRepository).save(user);
        verify(passwordEncoder, Mockito.never()).encode(any());
    }


}
