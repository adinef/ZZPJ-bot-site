package pl.lodz.p.it.zzpj.botsite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.repositories.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("mongoMessageService")
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message addMessage(Message message) throws MessageAdditionException {
            if (message.getId() != null) {
                throw new MessageAdditionException("Cannot add message with id specified.");
            }
            try {
                return this.messageRepository.save(message);
            } catch (final Exception e){
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
    public List<Message> getSingleMessageForUserById(Long userId, Long messageId) throws MessageRetrievalException {
        try {
            List<Message> messages = this.messageRepository.findByUserId(userId);
            return messages.stream().filter(m -> m.getId().equals(messageId)).collect(Collectors.toList());
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
    public Message updateMessage(Long userId, Long id, String content) throws MessageNotFoundException {
        try {
            Optional<Message> message = this.messageRepository.findById(id);
            if (!message.get().getUser().getId().equals(userId)) {
                throw new MessageNotFoundException("Message with that ID not found");
            }
            Message messageRetrieved = message.orElseThrow(() -> new MessageNotFoundException("Message with that ID not found."));
            messageRetrieved.setContent(content);
            return this.messageRepository.save(messageRetrieved);
        } catch (final Exception e) {
            throw new MessageNotFoundException("Message with that ID not found.", e);
        }
    }

    @Override
    public void deleteMessage(Long id) throws MessageDeletionException {
        try {
            this.messageRepository.deleteById(id);
        } catch (final Exception e) {
            throw new MessageDeletionException("Could not delete message.", e);
        }
    }
}
