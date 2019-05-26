package pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound;

public class VerificationTokenInfoNotFoundException extends Exception {
    public VerificationTokenInfoNotFoundException() {
    }

    public VerificationTokenInfoNotFoundException(String message) {
        super(message);
    }

    public VerificationTokenInfoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationTokenInfoNotFoundException(Throwable cause) {
        super(cause);
    }

}
