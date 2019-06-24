package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.UserTaskDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserTaskNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserTaskRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.UserTaskUpdateException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.UserTaskIdAlreadyExistsException;
import pl.lodz.p.it.zzpj.botsite.services.UserTaskService;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskAdminDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.usertasks.UserTaskUserDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/usertaskAdmin")
public class UserTaskAdminController {

    private final ModelMapper modelMapper;
    private final UserTaskService userTaskService;

    @Autowired
    public UserTaskAdminController(ModelMapper modelMapper,
                              UserTaskService userTaskService) {
        this.modelMapper = modelMapper;
        this.userTaskService = userTaskService;
    }

    //GET ALL FOR USER
    @Secured("ROLE_ADMIN")
    @GetMapping(
            value = "user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserTaskUserDTO> getAllByUserId(@PathVariable("userId") final Long userId)
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException {
        List<UserTaskUserDTO> userTaskUserDTOS = new ArrayList<>();
        List<UserTask> userTaskList = userTaskService.getListOfUserTasksByUserId(userId);
        modelMapper.map(userTaskList, userTaskUserDTOS);
        return userTaskUserDTOS;
    }

    // GET SINGLE TASK
    @Secured("ROLE_ADMIN")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskUserDTO getTaskByUserId(@PathVariable("id") final Long taskId)
            throws UserTaskRetrievalException {
        UserTaskUserDTO userTaskUserDTOS = new UserTaskUserDTO();
        UserTask userTask = userTaskService.findById(taskId);
        modelMapper.map(userTask, userTaskUserDTOS);
        return userTaskUserDTOS;
    }

    // ADD TASK
    @Secured("ROLE_ADMIN")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTaskAdminDTO addTask(@RequestBody UserTaskAdminDTO userTaskAdminDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException {
        UserTask userTask = this.modelMapper.map(userTaskAdminDTO, UserTask.class);
        UserTask addedUserTask = userTaskService.addUserTask(userTask);
        return modelMapper.map(addedUserTask, UserTaskAdminDTO.class);
    }

    // UPDATE TASK
    @Secured("ROLE_ADMIN")
    @PutMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskUserDTO editTask(@PathVariable("id") Long id, @RequestBody UserTaskUserDTO userTaskUserDTO) throws UserTaskUpdateException {
        UserTask userTask = this.modelMapper.map(userTaskUserDTO, UserTask.class);
        userTask.setId(id);
        UserTask updatedTask = this.userTaskService.update(userTask);
        return modelMapper.map(updatedTask, UserTaskUserDTO.class);
    }

    // DELETE TASK
    @Secured("ROLE_ADMIN")
    @DeleteMapping(
            value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserTask(@PathVariable("id") Long id) throws UserTaskDeletionException {
        this.userTaskService.deleteUserTask(id);
    }
}
