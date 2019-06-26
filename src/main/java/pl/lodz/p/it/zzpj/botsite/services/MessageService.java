package pl.lodz.p.it.zzpj.botsite.services;


import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageUpdateException;

import java.util.List;

public interface MessageService {

    Message addMessage(Message message) throws MessageAdditionException;

    List<Message> findAllByUserId(Long userId) throws MessageRetrievalException;

    Message findById(Long id) throws MessageRetrievalException, MessageNotFoundException;

    Message updateMessage(Message message) throws MessageUpdateException;

    void deleteMessage(Message message) throws MessageDeletionException;

}
