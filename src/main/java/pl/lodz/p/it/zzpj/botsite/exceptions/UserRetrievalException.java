package pl.lodz.p.it.zzpj.botsite.exceptions;

public class UserRetrievalException extends Exception {
    public UserRetrievalException() {
    }

    public UserRetrievalException(String message) {
        super(message);
    }

    public UserRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRetrievalException(Throwable cause) {
        super(cause);
    }
}
