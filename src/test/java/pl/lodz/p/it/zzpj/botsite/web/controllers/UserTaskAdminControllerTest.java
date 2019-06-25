package pl.lodz.p.it.zzpj.botsite.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
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
import pl.lodz.p.it.zzpj.botsite.entities.Bot;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskAdminDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class UserTaskAdminControllerTest {

    private MockMvc mockMvc;

    private RestResponseEntityExceptionHandler exceptionHandler = new RestResponseEntityExceptionHandler(HttpHeaders.EMPTY);

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserTaskService userTaskService;

    private ModelMapper realModelMapper = new ModelMapper();

    @InjectMocks
    private UserTaskAdminController userTaskAdminController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userTaskAdminController)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void getAllByUserIdShouldWorkAsExpected() throws Exception {
        List<UserTaskAdminDTO> userTaskAdminDTOS = new ArrayList<>();
        List<UserTask> userTaskList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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
        when(userTaskService.getListOfUserTasksByUserId(anyLong())).thenReturn(userTaskList);



        mockMvc.perform(
                get("/api/admin/usertask/user/0/")
        ).andExpect(status().isOk());

        verify(userTaskService).getListOfUserTasksByUserId(anyLong());
    }

    @Test
    void getAllByUserIdShouldReturnEmptyResultWhenNotFound() throws Exception {
        List<UserTask> userTaskList = new ArrayList<>();

        when(userTaskService.getListOfUserTasksByUserId(anyLong())).thenReturn(userTaskList);

        mockMvc.perform(
                get("/api/admin/usertask/user/0/")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());

        verify(userTaskService).getListOfUserTasksByUserId(anyLong());
    }

    @Test
    void getTaskByUserIdShouldWorkAsExpected() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
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
                get("/api/admin/usertask/0/")
        ).andExpect(status().isOk());

        verify(userTaskService).findById(any());
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
                .user(user)
                .reminderDate(LocalDateTime.parse("02-02-2018 12:00:11", formatter))
                .creationDate(LocalDateTime.parse("01-02-2018 12:00:11", formatter))
                .build();

        UserTaskAdminDTO dto = this.realModelMapper.map(userTask, UserTaskAdminDTO.class);

        when(userTaskService.addUserTask(any())).thenReturn(userTask);
        when(this.modelMapper.map(dto, UserTask.class)).thenReturn(userTask);
        when(this.modelMapper.map(userTask, UserTaskAdminDTO.class)).thenReturn(dto);
        mockMvc.perform(
                post("/api/admin/usertask")
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
                .user(user)
                .reminderDate(LocalDateTime.parse("02-02-2018 12:00:11", formatter))
                .creationDate(LocalDateTime.parse("01-02-2018 12:00:11", formatter))
                .build();

        UserTaskAdminDTO dto = this.realModelMapper.map(userTask, UserTaskAdminDTO.class);

        when(userTaskService.update(any(UserTask.class))).thenReturn(userTask);
        when(this.modelMapper.map(dto, UserTask.class)).thenReturn(userTask);
        when(this.modelMapper.map(userTask, UserTaskAdminDTO.class)).thenReturn(dto);

        mockMvc.perform(
                put("/api/admin/usertask/edit/0/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk());
        verify(userTaskService).update(any(UserTask.class));

    }

    @Test
    void deleteUserTaskShouldWorkAsExpected() throws Exception {
        doNothing().when(userTaskService).deleteUserTask(anyLong());
        mockMvc.perform(
                delete("/api/admin/usertask/0/")
        ).andExpect(status().isOk());
        verify(userTaskService).deleteUserTask(anyLong());
    }
}