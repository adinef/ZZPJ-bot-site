package pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound;

public class UserTaskNotFoundException extends NotFoundException {
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
}
