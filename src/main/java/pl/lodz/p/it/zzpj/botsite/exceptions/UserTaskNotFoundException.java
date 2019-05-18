package pl.lodz.p.it.zzpj.botsite.exceptions;

public class UserTaskNotFoundException extends Exception {
    public UserTaskNotFoundException() {
    }

    public UserTaskNotFoundException(String message) {
        super(message);
    }

    public UserTaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserTaskNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
