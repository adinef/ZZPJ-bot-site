package pl.lodz.p.it.zzpj.botsite.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        this.userService = new UserServiceImpl(userRepository);
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
}