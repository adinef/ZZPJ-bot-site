package pl.lodz.p.it.zzpj.botsite.services;


import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;

import java.util.List;

public interface MessageService {

    Message addMessage(Message message) throws MessageAdditionException;
    List<Message> getAllByUserId(String userId) throws MessageRetrievalException;
    Message findById(Long id) throws MessageRetrievalException;
    Message updateMessage(Long id, String content) throws MessageNotFoundException;
    void deleteMessage(Long id) throws MessageDeletionException;

}