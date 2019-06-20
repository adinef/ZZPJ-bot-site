package pl.lodz.p.it.zzpj.botsite.services;


import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;

import java.util.List;

public interface MessageService {

    Message addMessage(Message message) throws MessageAdditionException;
    List<Message> getAllByUserId(Long userId) throws MessageRetrievalException;

    List<Message> getSingleMessageForUserById(Long userId, Long messageId) throws MessageRetrievalException;

    Message findById(Long id) throws MessageRetrievalException;

    Message updateMessage(Long userId, Long id, String content) throws MessageNotFoundException;

    void deleteMessage(Long userId, Long id) throws MessageDeletionException;

}
