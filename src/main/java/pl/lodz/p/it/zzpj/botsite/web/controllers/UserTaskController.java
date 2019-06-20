package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.UserRole;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.BotService;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MyUserDetails;
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
    private final PrincipalProvider principalProvider;

    @Autowired
    public UserTaskController(ModelMapper modelMapper,
                              UserTaskService userTaskService,
                              PrincipalProvider principalProvider) {
        this.modelMapper = modelMapper;
        this.userTaskService = userTaskService;
        this.principalProvider = principalProvider;
    }

    //SECURITY + GET ALL FOR USER
    @Secured("ROLE_USER")
    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserTaskDTO> getAllTaskForCurrentUser()
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException {
        List<UserTaskDTO> userTaskDTOs = new ArrayList<>();
        List<UserTask> userTaskList = userTaskService.getListOfUserTasksByUserId(principalProvider.getUserId());
        modelMapper.map(userTaskList, userTaskDTOs);
        return userTaskDTOs;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
        userTask.setUser(((MyUserDetails)principalProvider.getPrincipal()).getUser());
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
        userTask.setUser(((MyUserDetails)principalProvider.getPrincipal()).getUser());
        UserTask updatedTask = this.userTaskService.update(principalProvider.getUserId(), userTask);
        return modelMapper.map(updatedTask, UserTaskDTO.class);
    }
}
