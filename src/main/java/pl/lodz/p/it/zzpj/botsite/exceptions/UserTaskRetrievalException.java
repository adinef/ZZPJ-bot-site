package pl.lodz.p.it.zzpj.botsite.exceptions;

public class UserTaskRetrievalException extends Exception {
    public UserTaskRetrievalException() {
    }

    public UserTaskRetrievalException(String message) {
        super(message);
    }

    public UserTaskRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskRetrievalException(Throwable cause) {
        super(cause);
    }

    public UserTaskRetrievalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
