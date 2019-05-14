package pl.lodz.p.it.zzpj.botsite.exceptions;

public class BotNotFoundException extends Exception {
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
