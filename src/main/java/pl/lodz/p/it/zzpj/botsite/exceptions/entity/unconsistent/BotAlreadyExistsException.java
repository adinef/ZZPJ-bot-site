package pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent;

public class BotAlreadyExistsException extends StateNotConsistentException {

    public BotAlreadyExistsException(String message) {
        super(message);
    }

    public BotAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
