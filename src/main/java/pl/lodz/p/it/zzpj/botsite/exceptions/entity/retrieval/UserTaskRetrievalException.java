package pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval;

public class UserTaskRetrievalException extends RetrievalTimeException {
    public UserTaskRetrievalException() {
    }

    public UserTaskRetrievalException(String message) {
        super(message);
    }

    public UserTaskRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserTaskRetrievalException(Throwable cause) {
        super(cause);
    }
}
