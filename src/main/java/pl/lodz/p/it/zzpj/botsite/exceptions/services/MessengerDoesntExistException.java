package pl.lodz.p.it.zzpj.botsite.exceptions.services;

import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.NotFoundException;

public class MessengerDoesntExistException extends NotFoundException {
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
