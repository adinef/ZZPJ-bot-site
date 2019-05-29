package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class MessageAdditionException extends SavingException {

    public MessageAdditionException() {
    }

    public MessageAdditionException(String message) {
        super(message);
    }

    public MessageAdditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageAdditionException(Throwable cause) {
        super(cause);
    }

}
