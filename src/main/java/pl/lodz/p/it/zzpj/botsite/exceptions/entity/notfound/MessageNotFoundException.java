package pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound;

public class MessageNotFoundException extends NotFoundException {
    public MessageNotFoundException() {
    }

    public MessageNotFoundException(String message) {
        super(message);
    }

    public MessageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageNotFoundException(Throwable cause) {
        super(cause);
    }
}

