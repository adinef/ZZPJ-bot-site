package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskAdminDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTaskAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserTaskService userTaskService;

    private ModelMapper realModelMapper = new ModelMapper();
    private Gson gson = new Gson();

    @InjectMocks
    private UserTaskAdminController userTaskAdminController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userTaskAdminController)
                .build();
    }

    @Test
    void getAllByUserId() throws Exception {
        List<UserTaskAdminDTO> userTaskAdminDTOS = new ArrayList<>();
        List<UserTask> userTaskList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String c = LocalDateTime.now().format(formatter);
        String r = LocalDateTime.now().plusDays(2).format(formatter);
        LocalDateTime cr = LocalDateTime.parse(c,formatter);
        LocalDateTime re = LocalDateTime.parse(r,formatter);
        Long id = 0L;
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
                .reminderDate(re)
                .creationDate(cr)
                .build();

        userTaskList.add(userTask);
        UserTaskAdminDTO dto = this.realModelMapper.map(userTask, UserTaskAdminDTO.class);
        userTaskAdminDTOS.add(dto);

        java.lang.reflect.Type targetListType = new TypeToken<List<UserTaskAdminDTO>>() {}.getType();
        when(userTaskService.getListOfUserTasksByUserId(anyLong())).thenReturn(userTaskList);
        when(modelMapper.map(userTaskList, targetListType)).thenReturn(userTaskAdminDTOS);



        mockMvc.perform(
                get("/api/usertaskAdmin/user/0/")
        ).andExpect(status().isOk());

        verify(userTaskService).getListOfUserTasksByUserId(anyLong());
    }

    @Test
    void getTaskByUserId() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String c = LocalDateTime.now().format(formatter);
        String r = LocalDateTime.now().plusDays(2).format(formatter);
        LocalDateTime cr = LocalDateTime.parse(c,formatter);
        LocalDateTime re = LocalDateTime.parse(r,formatter);
        Long id = 0L;
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
                .reminderDate(re)
                .creationDate(cr)
                .build();

        UserTaskAdminDTO dto = this.realModelMapper.map(userTask, UserTaskAdminDTO.class);

        when(userTaskService.findById(any())).thenReturn(userTask);
        when(modelMapper.map(userTask, UserTaskAdminDTO.class)).thenReturn(dto);



        mockMvc.perform(
                get("/api/usertaskAdmin/0/")
        ).andExpect(status().isOk());

        verify(userTaskService).findById(any());
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
                .reminderDate(re)
                .creationDate(cr)
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