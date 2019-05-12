package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserRegistrationDto;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    UserService userService;

    private Gson gson = new Gson();
    private ModelMapper modelMapper = new ModelMapper();

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
        UserRegistrationDto dto = modelMapper.map(user, UserRegistrationDto.class);

        String json = gson.toJson(dto);

        mockMvc.perform(
                post("/api/user/newUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(userService).addUser(any(User.class));

    }

}
