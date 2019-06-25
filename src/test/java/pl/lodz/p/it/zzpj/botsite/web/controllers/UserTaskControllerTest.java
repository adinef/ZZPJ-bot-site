package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lodz.p.it.zzpj.botsite.config.errorhandling.RestResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskUserDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserTaskService userTaskService;
    @Mock
    private UserService userService;

    private ModelMapper realModelMapper = new ModelMapper();
    @Mock
    private PrincipalProvider principal;
    @InjectMocks
    private UserTaskController userTaskController;
    private RestResponseEntityExceptionHandler exceptionHandler = new RestResponseEntityExceptionHandler(HttpHeaders.EMPTY);
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userTaskController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void getAllByUserIdShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        List<UserTaskUserDTO> userTaskUserDTOS = new ArrayList<>();
        List<UserTask> userTaskList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
                .reminderDate(LocalDateTime.parse("02-02-2018 12:00:11", formatter))
                .creationDate(LocalDateTime.parse("01-02-2018 12:00:11", formatter))
                .build();

        userTaskList.add(userTask);
        UserTaskUserDTO dto = this.realModelMapper.map(userTask, UserTaskUserDTO.class);
        userTaskUserDTOS.add(dto);

        java.lang.reflect.Type targetListType = new TypeToken<List<UserTaskUserDTO>>() {}.getType();
        when(this.userService.findByLogin(principal.getName())).thenReturn(user);
        when(userTaskService.getListOfUserTasksByUserId(anyLong())).thenReturn(userTaskList);
        when(modelMapper.map(userTaskList, targetListType)).thenReturn(userTaskUserDTOS);
        mockMvc.perform(
                get("/api/usertask/user/0/")
        ).andExpect(status().isOk());

        verify(userTaskService).getListOfUserTasksByUserId(anyLong());
    }

    @Test
    void addTaskShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
                .reminderDate(LocalDateTime.parse("02-02-2018 12:00:11", formatter))
                .creationDate(LocalDateTime.parse("01-02-2018 12:00:11", formatter))
                .build();

        UserTaskUserDTO dto = this.realModelMapper.map(userTask, UserTaskUserDTO.class);

        when(this.userService.findByLogin(principal.getName())).thenReturn(user);
        when(userTaskService.addUserTask(any())).thenReturn(userTask);
        when(this.modelMapper.map(dto, UserTask.class)).thenReturn(userTask);
        when(this.modelMapper.map(userTask, UserTaskUserDTO.class)).thenReturn(dto);

        mockMvc.perform(
                post("/api/usertask")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isCreated());
        verify(userTaskService).addUserTask(any(UserTask.class));
    }

    @Test
    void editTaskShouldWorkAsExpected() throws Exception {
        Long id = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
                .reminderDate(LocalDateTime.parse("02-02-2018 12:00:11", formatter))
                .creationDate(LocalDateTime.parse("01-02-2018 12:00:11", formatter))
                .build();

        UserTaskUserDTO dto = this.realModelMapper.map(userTask, UserTaskUserDTO.class);
        when(this.userTaskService.findById(0L)).thenReturn(UserTask.builder().id(0L).user(User.builder().id(0L).build()).build());
        when(this.userService.findByLogin(principal.getName())).thenReturn(user);
        when(userTaskService.update(any(UserTask.class))).thenReturn(userTask);
        when(this.modelMapper.map(dto, UserTask.class)).thenReturn(userTask);
        when(this.modelMapper.map(userTask, UserTaskUserDTO.class)).thenReturn(dto);

        mockMvc.perform(
                put("/api/usertask/edit/0/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());
        verify(userTaskService).update(any(UserTask.class));
    }

    @Test
    void deleteUserTaskShouldWorkAsExpected() throws Exception {
        User user = User.builder()
                .id(0L)
                .login("login")
                .build();
        when(this.userTaskService.findById(0L)).thenReturn(UserTask.builder().id(0L).user(User.builder().id(0L).build()).build());
        when(this.userService.findByLogin(principal.getName())).thenReturn(user);
        doNothing().when(userTaskService).deleteUserTask(anyLong());
        mockMvc.perform(
                delete("/api/usertask/0/")
        ).andExpect(status().isOk());
        verify(userTaskService).deleteUserTask(anyLong());
    }
}