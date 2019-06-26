package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.UserTaskDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskUserDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usertask")
public class UserTaskController {

    private final ModelMapper modelMapper;
    private final UserTaskService userTaskService;
    private final PrincipalProvider principalProvider;
    private final UserService userService;

    @Autowired
    public UserTaskController(ModelMapper modelMapper,
                              UserTaskService userTaskService,
                              PrincipalProvider principalProvider,
                              UserService userService) {
        this.modelMapper = modelMapper;
        this.userTaskService = userTaskService;
        this.principalProvider = principalProvider;
        this.userService = userService;
    }

    //SECURITY + GET ALL FOR USER
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(
            value = "user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserTaskUserDTO> getAllByCurrentUser()
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException, UserRetrievalException, UserTaskRetrievalException {
        User user = this.userService.findByLogin(this.principalProvider.getName());
        List<UserTask> userTaskList = this.userTaskService.getListOfUserTasksByUserId(user.getId());
        return mapList(userTaskList);
    }

    //SECURITY + GET SINGLE TASK
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskUserDTO getTaskById(@PathVariable("id") final Long taskId)
            throws UserTaskRetrievalException, UserRetrievalException {
        UserTask userTask = this.userTaskService.findById(taskId);
        User user = this.userService.findByLogin(this.principalProvider.getName());
        if (!userTask.getUser().getId().equals(user.getId())) {
            throw new UserTaskRetrievalException("Can not get User Task");
        }
        return this.modelMapper.map(userTask, UserTaskUserDTO.class);
    }

    // SECURITY
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserTaskUserDTO addTask(@RequestBody UserTaskUserDTO userTaskUserDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException, UserRetrievalException {
        UserTask userTask = this.modelMapper.map(userTaskUserDTO, UserTask.class);
        userTask.setUser(this.userService.findByLogin(this.principalProvider.getName()));
        UserTask addedUserTask = this.userTaskService.addUserTask(userTask);
        return this.modelMapper.map(addedUserTask, UserTaskUserDTO.class);
    }

    // CHECK IF USER HAS RIGHT TO UPDATE THE TASK
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskUserDTO editTask(@PathVariable("id") Long id, @RequestBody UserTaskUserDTO userTaskUserDTO) throws UserTaskUpdateException, UserRetrievalException, UserTaskRetrievalException {
        UserTask userTask = this.modelMapper.map(userTaskUserDTO, UserTask.class);
        User user = this.userService.findByLogin(this.principalProvider.getName());
        if (!this.userTaskService.findById(id).getUser().getId().equals(user.getId())) {
            throw new UserTaskUpdateException("User task cannot be updated.");
        }
        userTask.setId(id);
        UserTask updatedTask = this.userTaskService.update(userTask);
        return this.modelMapper.map(updatedTask, UserTaskUserDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(
            value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserTask(@PathVariable("id") Long id) throws UserTaskDeletionException, UserRetrievalException, UserTaskRetrievalException {
        User user = this.userService.findByLogin(this.principalProvider.getName());
        if (this.userTaskService.findById(id).getUser().getId().equals(user.getId())) {
            this.userTaskService.deleteUserTask(id);
        } else {
            throw new UserTaskDeletionException("User task cannot be deleted.");
        }
    }
    private List<UserTaskUserDTO> mapList(List<UserTask> userTasks) {
        List<UserTaskUserDTO> dtoList = new ArrayList<>();
        userTasks.forEach(b -> this.modelMapper.map(b, UserTaskUserDTO.class));
        return dtoList;
    }
}
