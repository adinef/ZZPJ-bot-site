package pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent;

public class UsernameAlreadyExistsException extends StateNotConsistentException {
    
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
