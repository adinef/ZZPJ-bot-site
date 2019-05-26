package pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent;

public class UserTaskStatusException extends StateNotConsistentException {
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
}
