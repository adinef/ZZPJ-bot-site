package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class MessageUpdateException extends SavingException {

    public MessageUpdateException() {
    }

    public MessageUpdateException(String message) {
        super(message);
    }

    public MessageUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageUpdateException(Throwable cause) {
        super(cause);
    }

}
