package pl.lodz.p.it.zzpj.botsite.web.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;
import pl.lodz.p.it.zzpj.botsite.web.dto.UserTaskDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final PrincipalProvider principalProvider;

    @Autowired
    public MessageController(ModelMapper modelMapper,
                             MessageService messageService,
                             PrincipalProvider principalProvider) {
        this.modelMapper = modelMapper;
        this.messageService = messageService;
        this.principalProvider = principalProvider;
    }


    @Secured("ROLE_USER")
    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MessageDTO> getAllMessagesForCurrentUser() throws MessageRetrievalException {
        List<MessageDTO> MessageDTOs = new ArrayList<>();
        List<Message> messageList = messageService.getAllByUserId(principalProvider.getUserId());
        modelMapper.map(messageList, MessageDTOs);
        return MessageDTOs;
    }


    @Secured("ROLE_USER")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO addMessage(@RequestBody MessageDTO messageDTO) throws MessageAdditionException, UserRetrievalException {
        Message message = this.modelMapper.map(messageDTO, Message.class);
        Message addedMessage = messageService.addMessage(principalProvider.getUserId(), message);
        return modelMapper.map(addedMessage, MessageDTO.class);
    }

    @Secured("ROLE_USER")
    @PutMapping(
            value = "edit/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageDTO editMessage(@PathVariable Long id, @RequestBody MessageDTO messageDto)
            throws MessageNotFoundException, MessageUpdateException {
        Message editedMessage = messageService.updateMessage(principalProvider.getUserId(), id, messageDto.getContent());
        return modelMapper.map(editedMessage, MessageDTO.class);
    }

    @Secured("ROLE_USER")
    @DeleteMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void deleteMessage(@PathVariable Long id, @RequestBody MessageDTO messageDto) throws MessageDeletionException {
        messageService.deleteMessage(principalProvider.getUserId(), id);
    }
}
