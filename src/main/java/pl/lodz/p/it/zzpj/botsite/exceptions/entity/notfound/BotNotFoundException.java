package pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound;

public class BotNotFoundException extends NotFoundException {
    public BotNotFoundException() {
    }

    public BotNotFoundException(String message) {
        super(message);
    }

    public BotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotNotFoundException(Throwable cause) {
        super(cause);
    }
}
