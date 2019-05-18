package pl.lodz.p.it.zzpj.botsite.exceptions;

public class UserTaskStatusException extends Exception {
    public UserTaskStatusException() {
    }

    public UserTaskStatusException(String message) {
        super(message);
    }

    public UserTaskStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskStatusException(Throwable cause) {
        super(cause);
    }

    public UserTaskStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
