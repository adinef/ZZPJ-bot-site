package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.services.VerificationTokenService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserUpdateDto;

import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Spy
    private ModelMapper modelMapperMock;

    @Mock
    private VerificationTokenService verificationTokenService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private PrincipalProvider principalProvider;

    private ModelMapper realModelMapper = new ModelMapper();
    private Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void registerUserShouldWorkAsExpected() throws Exception {
        User user = User.builder()
                .login("Login")
                .password("Password")
                .email("e123@email.ioio")
                .build();
        UserRegistrationDto dto = realModelMapper.map(user, UserRegistrationDto.class);
        when(modelMapperMock.map(dto, User.class)).thenReturn(user);
        when(userService.registerUser(any())).thenReturn(user);
        String json = gson.toJson(dto);
        mockMvc.perform(
                post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());
        verify(userService).registerUser(any(User.class));
    }

    @Test
    public void updateUserShouldWorkAsExpected() throws Exception {


        String newName = "newName";
        String newLastName = "newLastName";
        String newEmail = "newEmail@hmail.ioio";

        User userBeforeUpdate = User
                .builder()
                .login("Login")
                .id(Long.valueOf(99))
                .email("someMail@mail.ioio")
                .name("Name")
                .lastName("LastName")
                .password("hashedPassword")
                .build();


        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name(newName)
                .lastName(newLastName)
                .email(newEmail)
                .build();

        User updateUser = User
                .builder()
                .login(userBeforeUpdate.getLogin())
                .name(updateDto.getName())
                .lastName(updateDto.getLastName())
                .email(updateDto.getEmail())
                .roles(new ArrayList<>())
                .build();


        String json = gson.toJson(updateDto);

        when(principalProvider.getName()).thenReturn(userBeforeUpdate.getLogin());
        when(userService.findByLogin(userBeforeUpdate.getLogin())).thenReturn(userBeforeUpdate);
        spy(modelMapperMock).map(updateDto, userBeforeUpdate);


        mockMvc.perform(
                put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(userService).updateUser(updateUser, userBeforeUpdate.getId());

    }


}
