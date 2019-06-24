package pl.lodz.p.it.zzpj.botsite.web.controllers;


import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.config.security.PrincipalProvider;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.services.UserService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final UserService userService;
    private final PrincipalProvider principalProvider;

    @Autowired
    public MessageController(ModelMapper modelMapper,
                             MessageService messageService,
                             UserService userService,
                             PrincipalProvider principalProvider) {
        this.modelMapper = modelMapper;
        this.messageService = messageService;
        this.userService = userService;
        this.principalProvider = principalProvider;
    }


    @Secured("ROLE_USER")
    @GetMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDTO> getAllMessagesForCurrentUser() throws MessageRetrievalException {
        List<MessageDTO> MessageDTOs = new ArrayList<>();
        List<Message> messageList = messageService.findAllByUserId(principalProvider.getUserId());
        modelMapper.map(messageList, MessageDTOs);
        return MessageDTOs;
    }

    @Secured("ROLE_USER")
    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO getMessageByIdForCurrentUser(@PathVariable("id") Long messageId) throws MessageNotFoundException, MessageRetrievalException {
        Message message = messageService.findById(messageId);
        if (!message.getUser().getId().equals(principalProvider.getUserId())) {
            throw new MessageNotFoundException("No message with given ID for user");
        }
        return modelMapper.map(message, MessageDTO.class);
    }

    @Secured("ROLE_USER")
    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO addMessage(@RequestBody MessageDTO messageDTO) throws UserRetrievalException, MessageAdditionException {
        if (StringUtils.isBlank(messageDTO.getContent())) {
            throw new MessageAdditionException("Content cannot be blank.");
        }
        Message message = this.modelMapper.map(messageDTO, Message.class);
        User user = userService.findByLogin(principalProvider.getName());
        message.setUser(user);
        Message addedMessage = messageService.addMessage(message);
        return modelMapper.map(addedMessage, MessageDTO.class);
    }

    @Secured("ROLE_USER")
    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO editMessage(@PathVariable("id") Long messageId, @RequestBody MessageDTO messageDto)
            throws MessageUpdateException, MessageRetrievalException {
        if (StringUtils.isBlank(messageDto.getContent())) {
            throw new MessageUpdateException("Content is blank");
        }
        Message message = messageService.findById(messageId);
        if (!message.getUser().getId().equals(principalProvider.getUserId())) {
            throw new MessageUpdateException("You are not authorized to change someone else's message");
        }
        message.setContent(messageDto.getContent());
        Message editedMessage = messageService.updateMessage(message);
        return modelMapper.map(editedMessage, MessageDTO.class);
    }

    @Secured("ROLE_USER")
    @DeleteMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteMessage(@PathVariable("id") Long messageId) throws MessageDeletionException, MessageRetrievalException {
        Message message = messageService.findById(messageId);
        if (!message.getUser().getId().equals(principalProvider.getUserId())) {
            throw new MessageDeletionException("You are not authorized to delete someone else's message");
        }
        messageService.deleteMessage(message);
    }
}
