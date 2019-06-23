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
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageAdminController {

    private final ModelMapper modelMapper;
    private final MessageService messageService;

    @Autowired
    public MessageAdminController(ModelMapper modelMapper,
                                  MessageService messageService) {
        this.modelMapper = modelMapper;
        this.messageService = messageService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
            value = "user/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MessageDTO> getAllMessagesByUserId(@PathVariable("userId") final Long userId)
            throws MessageRetrievalException {
        List<MessageDTO> MessageDTOs = new ArrayList<>();
        List<Message> messageList = messageService.getAllByUserId(userId);
        modelMapper.map(messageList, MessageDTOs);
        return MessageDTOs;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
            value = "user/{userId}/message/{messageId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<MessageDTO> getUsersMessageById(@PathVariable("userId") final Long userId,
                                                @PathVariable("messageId") final Long messageId)
            throws MessageRetrievalException {
        List<MessageDTO> MessageDTOs = new ArrayList<>();
        Message messageList = messageService.getSingleMessageForUserById(userId, messageId);
        modelMapper.map(messageList, MessageDTOs);
        return MessageDTOs;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(
            value = "user/{userId}/message/{messageId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageDTO editMessageForUser(@PathVariable("userId") Long userId, @PathVariable("messageId") Long messageId,
                                         @RequestBody MessageDTO messageDto) throws MessageNotFoundException, MessageUpdateException {
        Message editedMessage = messageService.updateMessage(userId, messageId, messageDto.getContent());
        return modelMapper.map(editedMessage, MessageDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(
            value = "user/{userId}/message/{messageId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void deleteMessageForUser(@PathVariable("userId") Long userId, @PathVariable("messageId") Long messageId) throws MessageDeletionException {
        messageService.deleteMessage(userId, messageId);
    }
}
