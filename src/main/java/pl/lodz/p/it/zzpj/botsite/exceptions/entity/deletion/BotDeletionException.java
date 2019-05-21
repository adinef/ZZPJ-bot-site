package pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion;

public class BotDeletionException extends DeletionException {
    public BotDeletionException() {
    }

    public BotDeletionException(String message) {
        super(message);
    }

    public BotDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotDeletionException(Throwable cause) {
        super(cause);
    }
}
