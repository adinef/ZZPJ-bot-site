package pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval;

public class RetrievalTimeException extends Exception {
    public RetrievalTimeException() {
    }

    public RetrievalTimeException(String message) {
        super(message);
    }

    public RetrievalTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetrievalTimeException(Throwable cause) {
        super(cause);
    }
}
