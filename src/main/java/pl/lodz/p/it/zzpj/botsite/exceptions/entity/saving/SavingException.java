package pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving;

public class SavingException extends Exception{
    public SavingException() {
    }

    public SavingException(String message) {
        super(message);
    }

    public SavingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingException(Throwable cause) {
        super(cause);
    }
}
