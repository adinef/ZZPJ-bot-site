package pl.lodz.p.it.zzpj.botsite.services;


import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.UserNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.UserRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;

import java.util.List;

public interface MessageService {

    Message addMessage(Long userId, Message message) throws MessageAdditionException, UserNotFoundException;
    List<Message> getAllByUserId(Long userId) throws MessageRetrievalException;

    Message getSingleMessageForUserById(Long userId, Long messageId) throws MessageRetrievalException;

    Message findById(Long id) throws MessageRetrievalException;

    Message updateMessage(Long userId, Long id, String content) throws MessageNotFoundException, MessageUpdateException;

    void deleteMessage(Long userId, Long id) throws MessageDeletionException;

}
