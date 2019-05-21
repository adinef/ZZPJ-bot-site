package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class BotUpdateException extends SavingException {
    public BotUpdateException() {
    }

    public BotUpdateException(String message) {
        super(message);
    }

    public BotUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotUpdateException(Throwable cause) {
        super(cause);
    }
}
