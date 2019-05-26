package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class BotAdditionException extends SavingException {
    public BotAdditionException() {
    }

    public BotAdditionException(String message) {
        super(message);
    }

    public BotAdditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotAdditionException(Throwable cause) {
        super(cause);
    }
}
