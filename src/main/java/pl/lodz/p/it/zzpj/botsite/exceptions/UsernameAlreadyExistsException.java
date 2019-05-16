package pl.lodz.p.it.zzpj.botsite.exceptions;

public class UsernameAlreadyExistsException extends Exception {
    
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
