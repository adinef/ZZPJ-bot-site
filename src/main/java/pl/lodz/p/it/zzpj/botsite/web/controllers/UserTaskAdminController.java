package pl.lodz.p.it.zzpj.botsite.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
@Slf4j
@RequestMapping("/api/admin/usertask")
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
    public List<UserTaskAdminDTO> getAllByUserId(@PathVariable("userId") final Long userId)
            throws UserTaskNotFoundException, UserNotFoundException, UserTaskUpdateException {
        List<UserTask> userTaskList = this.userTaskService.getListOfUserTasksByUserId(userId);
        return mapList(userTaskList);
    }

    // GET SINGLE TASK
    @Secured("ROLE_ADMIN")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public UserTaskAdminDTO getTaskByUserId(@PathVariable("id") final Long taskId)
            throws UserTaskRetrievalException {
        UserTask userTask = this.userTaskService.findById(taskId);
        return this.modelMapper.map(userTask, UserTaskAdminDTO.class);
    }

    // ADD TASK
    @Secured("ROLE_ADMIN")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserTaskAdminDTO addTask(@RequestBody UserTaskAdminDTO userTaskAdminDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException {
        UserTask userTask = this.modelMapper.map(userTaskAdminDTO, UserTask.class);
        UserTask addedUserTask = this.userTaskService.addUserTask(userTask);
        return this.modelMapper.map(addedUserTask, UserTaskAdminDTO.class);
    }

    // UPDATE TASK
    @Secured("ROLE_ADMIN")
    @PutMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskAdminDTO editTask(@PathVariable("id") Long id, @RequestBody UserTaskAdminDTO userTaskAdminDTO) throws UserTaskUpdateException {
        UserTask userTask = this.modelMapper.map(userTaskAdminDTO, UserTask.class);
        userTask.setId(id);
        UserTask updatedTask = this.userTaskService.update(userTask);
        return this.modelMapper.map(updatedTask, UserTaskAdminDTO.class);
    }

    // DELETE TASK
    @Secured("ROLE_ADMIN")
    @DeleteMapping(
            value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserTask(@PathVariable("id") Long id) throws UserTaskDeletionException {
        this.userTaskService.deleteUserTask(id);
    }

    private List<UserTaskAdminDTO> mapList(List<UserTask> userTasks) {
        List<UserTaskAdminDTO> dtoList = new ArrayList<>();
        userTasks.forEach( b -> this.modelMapper.map(b, UserTaskUserDTO.class));
        return dtoList;
    }
}