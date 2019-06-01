package pl.lodz.p.it.zzpj.botsite.botservices.exception;

public class MessengerDoesntExistException extends Exception{
    public MessengerDoesntExistException() {
    }

    public MessengerDoesntExistException(String message) {
        super(message);
    }

    public MessengerDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessengerDoesntExistException(Throwable cause) {
        super(cause);
    }
}
