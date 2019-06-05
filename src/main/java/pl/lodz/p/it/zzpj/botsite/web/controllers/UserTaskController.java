package pl.lodz.p.it.zzpj.botsite.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.UserTask;
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

@RestController
@RequestMapping("/usertask")
public class UserTaskController {

    private final ModelMapper modelMapper;
    private final UserTaskService userTaskService;
    private final BotService botService;

    @Autowired
    public UserTaskController(ModelMapper modelMapper,
                              UserTaskService userTaskService,
                              BotService botService) {
        this.modelMapper = modelMapper;
        this.userTaskService = userTaskService;
        this.botService = botService;
    }

    @GetMapping(
            value = "all",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserTaskDTO> getAllByUserId(@RequestParam("userId") final String userId) throws UserTaskNotFoundException {
        List<UserTaskDTO> userTaskDTOs = new ArrayList<>();
        List<UserTask> userTaskList = userTaskService.getListOfUserTasksByUserId(userId);
        modelMapper.map(userTaskList, userTaskDTOs);
        return userTaskDTOs;
    }

    @PostMapping(
            value = "addtask",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTaskDTO addTask(@RequestBody UserTaskDTO userTaskDTO) throws UserTaskAdditionException, UserTaskIdAlreadyExistsException {
        UserTask userTask = this.modelMapper.map(userTaskDTO, UserTask.class);
        UserTask addedUserTask = userTaskService.addUserTask(userTask);
        return modelMapper.map(addedUserTask, UserTaskDTO.class);
    }

    @PostMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public UserTaskDTO editTask(@PathVariable String id, @RequestBody UserTaskDTO userTaskDTO) throws UserTaskNotFoundException, UserTaskUpdateException {
        UserTask userTask = this.modelMapper.map(userTaskDTO, UserTask.class);
        UserTask editedUserTask = userTaskService.updateDate(userTask.getId(), LocalDateTime.now()); //TODO
        return modelMapper.map(editedUserTask, UserTaskDTO.class);
    }
}
