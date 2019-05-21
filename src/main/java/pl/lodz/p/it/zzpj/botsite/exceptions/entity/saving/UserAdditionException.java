package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class UserAdditionException extends SavingException {
    public UserAdditionException() {
    }

    public UserAdditionException(String message) {
        super(message);
    }

    public UserAdditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAdditionException(Throwable cause) {
        super(cause);
    }
}
