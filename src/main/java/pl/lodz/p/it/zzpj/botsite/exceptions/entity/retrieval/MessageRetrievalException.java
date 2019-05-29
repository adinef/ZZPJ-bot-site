package pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval;

public class MessageRetrievalException extends RetrievalTimeException {
    public MessageRetrievalException() {
    }

    public MessageRetrievalException(String message) {
        super(message);
    }

    public MessageRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRetrievalException(Throwable cause) {
        super(cause);
    }
}
