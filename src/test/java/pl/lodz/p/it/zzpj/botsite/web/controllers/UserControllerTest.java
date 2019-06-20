package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    UserService userService;

    private Gson gson = new Gson();

    @Mock
    private ModelMapper modelMapper;

    private ModelMapper realModelMapper = new ModelMapper();


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
        when(modelMapper.map(dto, User.class)).thenReturn(user);
        when(userService.registerUser(any())).thenReturn(user);
        String json = gson.toJson(dto);
        mockMvc.perform(
                post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());
        verify(userService).registerUser(any(User.class));
    }

}
