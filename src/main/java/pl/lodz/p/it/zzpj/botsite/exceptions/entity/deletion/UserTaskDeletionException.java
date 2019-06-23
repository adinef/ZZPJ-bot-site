package pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion;

public class UserTaskDeletionException extends DeletionException {
    public UserTaskDeletionException() {
        super();
    }

    public UserTaskDeletionException(String message) {
        super(message);
    }

    public UserTaskDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskDeletionException(Throwable cause) {
        super(cause);
    }
}
