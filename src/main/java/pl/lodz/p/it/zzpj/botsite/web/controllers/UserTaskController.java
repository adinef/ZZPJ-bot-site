package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.UserRole;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserTaskDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pl.lodz.p.it.zzpj.botsite.entities.UserRole.SECURITY_ROLE;

@RestController
@RequestMapping("/api/usertask")
public class UserTaskController {

    private final ModelMapper modelMapper;
    private final UserTaskService userTaskService;

    @Autowired
    public UserTaskController(ModelMapper modelMapper,
                              UserTaskService userTaskService) {
        this.modelMapper = modelMapper;
        this.userTaskService = userTaskService;
    }

    //SECURITY + GET ALL FOR USER
    @Secured("ROLE_USER")
    @GetMapping(
            value = "user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserTaskDTO> getAllByUserId(@PathVariable("userId") final Long userId)
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException {
        List<UserTaskDTO> userTaskDTOs = new ArrayList<>();
        List<UserTask> userTaskList = userTaskService.getListOfUserTasksByUserId(userId);
        modelMapper.map(userTaskList, userTaskDTOs);
        return userTaskDTOs;
    }

    // SECURITY
    @Secured("ROLE_USER")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTaskDTO addTask(@RequestBody UserTaskDTO userTaskDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException {
        UserTask userTask = this.modelMapper.map(userTaskDTO, UserTask.class);
        UserTask addedUserTask = userTaskService.addUserTask(userTask);
        return modelMapper.map(addedUserTask, UserTaskDTO.class);
    }

    // CHECK IF USER HAS RIGHT TO UPDATE THE TASK
    @Secured("ROLE_USER")
    @PutMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskDTO editTask(@PathVariable("id") Long id, @RequestBody UserTaskDTO userTaskDTO) throws UserTaskUpdateException {
        UserTask userTask = this.modelMapper.map(userTaskDTO, UserTask.class);
        userTask.setId(id);
        UserTask updatedTask = this.userTaskService.update(userTask);
        return modelMapper.map(updatedTask, UserTaskDTO.class);
    }
}
