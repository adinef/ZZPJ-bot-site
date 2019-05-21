package pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent;

public class UserTaskIdAlreadyExistsException extends StateNotConsistentException{
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
}
