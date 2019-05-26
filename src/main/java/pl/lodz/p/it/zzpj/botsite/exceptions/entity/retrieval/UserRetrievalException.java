package pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval;

public class UserRetrievalException extends RetrievalTimeException {
    public UserRetrievalException() {
    }

    public UserRetrievalException(String message) {
        super(message);
    }

    public UserRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRetrievalException(Throwable cause) {
        super(cause);
    }
}
