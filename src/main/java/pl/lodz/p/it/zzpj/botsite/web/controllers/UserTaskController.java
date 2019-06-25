package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MyUserDetails;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserTaskDTO;

import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public List<UserTaskDTO> getAllByUserId(@PathVariable("userId") final Long userId)
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException {
        List<UserTaskDTO> userTaskDTOs = new ArrayList<>();
        List<UserTask> userTaskList = userTaskService.getListOfUserTasksByUserId(userId);
        modelMapper.map(userTaskList, userTaskDTOs);
        return userTaskDTOs;
    }

    // SECURITY
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserTaskDTO addTask(@RequestBody UserTaskDTO userTaskDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException {
        UserTask userTask = this.modelMapper.map(userTaskDTO, UserTask.class);
        userTask.setUser(((MyUserDetails)principalProvider.getPrincipal()).getUser());
        UserTask addedUserTask = userTaskService.addUserTask(userTask);
        return modelMapper.map(addedUserTask, UserTaskDTO.class);
    }

    // CHECK IF USER HAS RIGHT TO UPDATE THE TASK
    //TODO
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public UserTaskDTO editTask(@PathVariable("id") Long id, @RequestBody UserTaskDTO userTaskDTO) throws UserTaskUpdateException, UserTaskRetrievalException {
        //TODO -> OTHER VALIDATION STUFF?
        /*if (userTaskDTO.getReminderDate().isBefore(LocalDateTime.now())) {
            throw new DateTimeException("Cannot set reminder to this date");
        }*/
        UserTask task = this.userTaskService.findById(0L);//userTaskDTO.getId());
        if(!task.getUser().getId().equals(principalProvider.getUserId())) {
            throw new UserTaskUpdateException("You are not authorized to edit someone else's task");
        }
        //TODO -> SET DTO VALUES HERE
        UserTask updatedTask = this.userTaskService.update(task);
        return modelMapper.map(updatedTask, UserTaskDTO.class);
    }

}
