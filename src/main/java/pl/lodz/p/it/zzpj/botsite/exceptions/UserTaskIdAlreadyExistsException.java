package pl.lodz.p.it.zzpj.botsite.exceptions;

public class UserTaskIdAlreadyExistsException extends Exception{
    public UserTaskIdAlreadyExistsException() {
    }

    public UserTaskIdAlreadyExistsException(String message) {
        super(message);
    }

    public UserTaskIdAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskIdAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UserTaskIdAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
