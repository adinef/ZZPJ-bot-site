package pl.lodz.p.it.zzpj.botsite.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;
import pl.lodz.p.it.zzpj.botsite.repositories.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message addMessage(Message message) throws MessageAdditionException {
        try {
            return this.messageRepository.save(message);
        } catch (final Exception e) {
            throw new MessageAdditionException("Error occurred during addition of message.", e);
        }
    }

    @Override
    public List<Message> findAllByUserId(Long id) throws MessageRetrievalException {
        try {
            return this.messageRepository.findByUserId(id);
        } catch (final Exception e) {
            throw new MessageRetrievalException("Could not retrieve list of messages by user ID", e);
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
    public Message updateMessage(Message message) throws MessageUpdateException {
        try {
            return this.messageRepository.save(message);
        } catch (Exception e) {
            throw new MessageUpdateException("Could not update message.", e);
        }
    }

    @Override
    public void deleteMessage(Message message) throws MessageDeletionException {
        try {
            this.messageRepository.deleteById(message.getId());
        } catch (final Exception e) {
            throw new MessageDeletionException("Could not delete message.", e);
        }
    }
}
