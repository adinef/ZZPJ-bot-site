package pl.lodz.p.it.zzpj.botsite.exceptions;

public class BotRetrievalException extends Exception {
    public BotRetrievalException() {
    }

    public BotRetrievalException(String message) {
        super(message);
    }

    public BotRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotRetrievalException(Throwable cause) {
        super(cause);
    }
}
