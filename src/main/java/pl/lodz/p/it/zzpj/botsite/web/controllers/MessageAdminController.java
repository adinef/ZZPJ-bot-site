package pl.lodz.p.it.zzpj.botsite.web.controllers;


import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.services.MessageService;
import pl.lodz.p.it.zzpj.botsite.web.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

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
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDTO> getAllMessagesByUserId(@PathVariable("userId") final Long userId)
            throws MessageRetrievalException {
        List<Message> messages = messageService.findAllByUserId(userId);
        return mapAllMessages(messages);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
            value = "message/{messageId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO getUsersMessageById(@PathVariable("messageId") final Long messageId)
            throws MessageRetrievalException, MessageNotFoundException {
        Message message = messageService.findById(messageId);
        return modelMapper.map(message, MessageDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(
            value = "message/{messageId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO editMessageForUser(@PathVariable("messageId") Long messageId,
                                         @RequestBody MessageDTO messageDto) throws MessageRetrievalException, MessageUpdateException, MessageNotFoundException {
        if (StringUtils.isBlank(messageDto.getContent())) {
            throw new MessageUpdateException("Content is blank");
        }
        Message message = messageService.findById(messageId);
        message.setContent(messageDto.getContent());
        Message editedMessage = messageService.updateMessage(message);
        return modelMapper.map(editedMessage, MessageDTO.class);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(
            value = "/message/{messageId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteMessage(@PathVariable("messageId") Long messageId) throws MessageRetrievalException, MessageDeletionException, MessageNotFoundException {
        Message message = messageService.findById(messageId);
        messageService.deleteMessage(message);
    }

    private List<MessageDTO> mapAllMessages(List<Message> messages) {
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message m : messages) {
            messageDTOs.add(modelMapper.map(m, MessageDTO.class));
        }
        return messageDTOs;
    }
}
