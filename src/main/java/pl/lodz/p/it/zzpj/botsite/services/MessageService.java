package pl.lodz.p.it.zzpj.botsite.services;


import pl.lodz.p.it.zzpj.botsite.entities.Message;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.MessageDeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.MessageRetrievalException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.MessageAdditionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.MessageNotFoundException;

public interface MessageService {

    Message addMessage(Message message) throws MessageAdditionException;
    Message findById(String id) throws MessageRetrievalException;
    Message editMessage(String id, String content) throws MessageNotFoundException;
    void deleteMessage(String id) throws MessageDeletionException;

}
