package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class UserTaskUpdateException extends SavingException {
    public UserTaskUpdateException() {
    }

    public UserTaskUpdateException(String message) {
        super(message);
    }

    public UserTaskUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskUpdateException(Throwable cause) {
        super(cause);
    }
}
