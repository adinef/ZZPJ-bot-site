package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class UserUpdateException extends SavingException {
    public UserUpdateException() {
    }

    public UserUpdateException(String message) {
        super(message);
    }

    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserUpdateException(Throwable cause) {
        super(cause);
    }
}
