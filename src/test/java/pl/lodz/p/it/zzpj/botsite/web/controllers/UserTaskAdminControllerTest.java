package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskAdminDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTaskAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserTaskService userTaskService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ModelMapper realModelMapper = new ModelMapper();

    @InjectMocks
    private UserTaskAdminController userTaskAdminController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userTaskAdminController)
                .build();
    }

    @Test
    void getAllByUserId() {
    }

    @Test
    void getTaskByUserId() {
    }

    @Test
    void addTask() throws Exception {
        Long id = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Bot bot = Bot.builder()
                .id(id)
                .name("FirstBot")
                .channel("FirstChannel")
                .build();

        Message message = Message.builder()
                .id(id)
                .content("Content")
                .build();

        User user = User.builder()
                .id(id)
                .login("login")
                .build();

        UserTask userTask = UserTask.builder()
                .id(id)
                .bot(bot)
                .message(message)
                .user(user)
                .reminderDate(LocalDateTime.parse("02-02-2018 12:00:00", formatter))
                .creationDate(LocalDateTime.parse("01-02-2018 12:00:00", formatter))
                .build();

        UserTaskAdminDTO dto = this.realModelMapper.map(userTask, UserTaskAdminDTO.class);


        when(userTaskService.addUserTask(any())).thenReturn(userTask);
        when(this.modelMapper.map(dto, UserTask.class)).thenReturn(userTask);
        when(this.modelMapper.map(userTask, UserTaskAdminDTO.class)).thenReturn(dto);

        mockMvc.perform(
                post("/api/usertaskAdmin")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());

        verify(userTaskService).addUserTask(any(UserTask.class));
    }

    @Test
    void editTask() {
    }

    @Test
    void deleteUserTask() {
    }
}