package pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion;

public class MessageDeletionException extends DeletionException {
    public MessageDeletionException() {
    }

    public MessageDeletionException(String message) {
        super(message);
    }

    public MessageDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageDeletionException(Throwable cause) {
        super(cause);
    }
}

