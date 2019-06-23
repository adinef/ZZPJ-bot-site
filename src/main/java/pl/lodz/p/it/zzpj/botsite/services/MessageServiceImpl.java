package pl.lodz.p.it.zzpj.botsite.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.entities.User;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.repositories.MessageRepository;
import pl.lodz.p.it.zzpj.botsite.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("mongoMessageService")
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message addMessage(Long userId, Message message) throws MessageAdditionException, UserNotFoundException {
        if(StringUtils.isBlank(message.getContent())) throw new MessageAdditionException("Content cannot be blank.");
        Optional<User> user = userRepository.findById(userId);
        User retrievedUser = user.orElseThrow(() -> new UserNotFoundException("No user with ID specified exists."));
        message.setUser(retrievedUser);
        try {
            return this.messageRepository.save(message);
        } catch (final Exception e) {
            throw new MessageAdditionException("Error occurred during addition of message.", e);
        }
    }

    @Override
    public List<Message> getAllByUserId(Long id) throws MessageRetrievalException {
        try {
            return this.messageRepository.findByUserId(id);
        } catch (final Exception e) {
            throw new MessageRetrievalException("Could not retrieve list of messages by user ID", e);
        }
    }

    @Override
    public Message getSingleMessageForUserById(Long userId, Long messageId) throws MessageRetrievalException {
        try {
            List<Message> messages = this.messageRepository.findByUserId(userId);
            return messages.stream().filter(m -> m.getId().equals(messageId)).findFirst().orElseThrow(() -> new MessageRetrievalException("No message with given ID"));
        } catch (final Exception e) {
            throw new MessageRetrievalException("Message with given ID doesn't exist for this user", e);
        }
    }

    @Override
    public Message findById(Long id) throws MessageRetrievalException {
        try {
            Optional<Message> message = this.messageRepository.findById(id);
            return message
                    .orElseThrow(
                            () -> new MessageNotFoundException("Message with that ID not found.")
                    );
        } catch (final Exception e) {
            throw new MessageRetrievalException("Could not retrieve message by ID", e);
        }
    }

    @Override
    public Message updateMessage(Long userId, Long id, String content) throws MessageNotFoundException, MessageUpdateException {
        try {
            if (StringUtils.isBlank(content)) throw new MessageUpdateException("Content is blank");
            Optional<Message> message = this.messageRepository.findById(id);
            Message messageRetrieved = message.orElseThrow(() -> new MessageNotFoundException("Message with that ID not found."));
            if (!messageRetrieved.getUser().getId().equals(userId)) {
                throw new MessageNotFoundException("Message with that ID not found");
            }
            messageRetrieved.setContent(content);
            return this.messageRepository.save(messageRetrieved);
        } catch (final Exception e) {
            throw e;
        }
    }

    @Override
    public void deleteMessage(Long userId, Long id) throws MessageDeletionException {
        try {
            Message message = this.getSingleMessageForUserById(userId,id);
            this.messageRepository.deleteById(message.getId());
        } catch (final Exception e) {
            throw new MessageDeletionException("Could not delete message.", e);
        }
    }
}
