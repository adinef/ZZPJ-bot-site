package pl.lodz.p.it.zzpj.botsite.web.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserTaskDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Autowired
    public MessageController(ModelMapper modelMapper,
                             MessageService messageService) {
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }


    @GetMapping(
            value = "all",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MessageDTO> getAllByUserId(@RequestParam("userId") final String userId) throws MessageRetrievalException {
        List<MessageDTO> MessageDTOs = new ArrayList<>();
        List<Message> messageList = messageService.getAllByUserId(userId);
        modelMapper.map(messageList, MessageDTOs);
        return MessageDTOs;
    }

    @PostMapping(
            value = "addmessage",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO addMessage(@RequestBody MessageDTO messageDTO) throws MessageAdditionException {
        Message message = this.modelMapper.map(messageDTO, Message.class);
        Message addedMessage = messageService.addMessage(message);
        return modelMapper.map(addedMessage, MessageDTO.class);
    }

    @PostMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageDTO editTask(@PathVariable Long id, @RequestBody MessageDTO messageDto) throws MessageNotFoundException {
        Message editedMessage = messageService.updateMessage(id, messageDto.getContent());
        return modelMapper.map(editedMessage, MessageDTO.class);
    }
}
