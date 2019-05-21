package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class UserTaskAdditionException extends SavingException {
    public UserTaskAdditionException() {
    }

    public UserTaskAdditionException(String message) {
        super(message);
    }

    public UserTaskAdditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskAdditionException(Throwable cause) {
        super(cause);
    }
}
