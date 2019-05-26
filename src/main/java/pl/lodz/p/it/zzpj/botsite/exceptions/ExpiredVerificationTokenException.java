package pl.lodz.p.it.zzpj.botsite.exceptions;

public class ExpiredVerificationTokenException extends Exception {

    public ExpiredVerificationTokenException() {
    }

    public ExpiredVerificationTokenException(String message) {
        super(message);
    }

    public ExpiredVerificationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredVerificationTokenException(Throwable cause) {
        super(cause);
    }

}
