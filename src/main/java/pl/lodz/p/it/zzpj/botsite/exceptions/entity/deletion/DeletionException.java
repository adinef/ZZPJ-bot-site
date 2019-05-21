package pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion;

public class DeletionException extends Exception {
    public DeletionException() {
    }

    public DeletionException(String message) {
        super(message);
    }

    public DeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletionException(Throwable cause) {
        super(cause);
    }
}
