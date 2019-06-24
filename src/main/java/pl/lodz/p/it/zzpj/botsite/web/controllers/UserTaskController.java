package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
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
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskAdminDTO;
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
                              UserTaskService userTaskService, PrincipalProvider principalProvider, UserService userService) {
        this.modelMapper = modelMapper;
        this.userTaskService = userTaskService;
        this.principalProvider = principalProvider;
        this.userService = userService;
    }

    //SECURITY + GET ALL FOR USER
    @Secured("ROLE_USER")
    @GetMapping(
            value = "user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserTaskUserDTO> getAllByUserId(@PathVariable("userId") final Long userId)
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException, UserRetrievalException, UserTaskRetrievalException {
        User user = this.userService.findByLogin(principalProvider.getName());
        if (!user.getId().equals(userId)) {
            throw new UserTaskRetrievalException("Can not get User Tasks");
        }
        List<UserTaskUserDTO> userTaskUserDTOS = new ArrayList<>();
        List<UserTask> userTaskList = userTaskService.getListOfUserTasksByUserId(userId);
        modelMapper.map(userTaskList, userTaskUserDTOS);
        return userTaskUserDTOS;
    }

    //SECURITY + GET SINGLE TASK
    @Secured("ROLE_USER")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskUserDTO getTaskByUserId(@PathVariable("id") final Long taskId)
            throws UserTaskRetrievalException, UserRetrievalException {
        UserTaskUserDTO userTaskUserDTOS = new UserTaskUserDTO();
        UserTask userTask = userTaskService.findById(taskId);
        User user = this.userService.findByLogin(principalProvider.getName());
        if (!userTask.getUser().getId().equals(user.getId())) {
            throw new UserTaskRetrievalException("Can not get User Task");
        }
        modelMapper.map(userTask, userTaskUserDTOS);
        return userTaskUserDTOS;
    }

    // SECURITY
    @Secured("ROLE_USER")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTaskUserDTO addTask(@RequestBody UserTaskUserDTO userTaskUserDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException, UserRetrievalException {
        UserTask userTask = this.modelMapper.map(userTaskUserDTO, UserTask.class);
        userTask.setUser(userService.findByLogin(principalProvider.getName()));
        UserTask addedUserTask = userTaskService.addUserTask(userTask);
        return modelMapper.map(addedUserTask, UserTaskUserDTO.class);
    }

    // CHECK IF USER HAS RIGHT TO UPDATE THE TASK
    @Secured("ROLE_USER")
    @PutMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskUserDTO editTask(@PathVariable("id") Long id, @RequestBody UserTaskUserDTO userTaskUserDTO) throws UserTaskUpdateException, UserRetrievalException, UserTaskRetrievalException {
        UserTask userTask = this.modelMapper.map(userTaskUserDTO, UserTask.class);
        User user = this.userService.findByLogin(principalProvider.getName());
        if (!userTaskService.findById(id).getUser().getId().equals(user.getId())) {
            throw new UserTaskUpdateException("User task cannot be updated.");
        }
        userTask.setId(id);
        UserTask updatedTask = this.userTaskService.update(userTask);
        return modelMapper.map(updatedTask, UserTaskUserDTO.class);
    }

    @Secured("ROLE_USER")
    @DeleteMapping(
            value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserTask(@PathVariable("id") Long id) throws UserTaskDeletionException, UserRetrievalException, UserTaskRetrievalException {
        User user = this.userService.findByLogin(principalProvider.getName());
        if (userTaskService.findById(id).getUser().getId().equals(user.getId())) {
            this.userTaskService.deleteUserTask(id);
        } else {
            throw new UserTaskDeletionException("User task cannot be deleted.");
        }
    }
}
