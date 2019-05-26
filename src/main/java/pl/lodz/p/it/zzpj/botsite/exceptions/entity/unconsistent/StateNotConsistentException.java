package pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent;

public class StateNotConsistentException extends Exception {
    public StateNotConsistentException() {
    }

    public StateNotConsistentException(String message) {
        super(message);
    }

    public StateNotConsistentException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateNotConsistentException(Throwable cause) {
        super(cause);
    }
}
