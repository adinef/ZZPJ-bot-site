package pl.lodz.p.it.zzpj.botsite.config.errorhandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.DeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.NotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.RetrievalTimeException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.SavingException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.StateNotConsistentException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final HttpHeaders httpHeaders;

    @Autowired
    public RestResponseEntityExceptionHandler(@Qualifier("json") HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        String resp = "Resource not found.";
        return handleExceptionInternal(
                ex, new Error(resp, ex), httpHeaders, HttpStatus.NOT_FOUND, request
        );
    }

    @ExceptionHandler(value = {RetrievalTimeException.class})
    protected ResponseEntity<Object> handleCouldNotRetrieve(RuntimeException ex, WebRequest request) {
        String resp = "Resource could not be retrieved.";
        return handleExceptionInternal(
                ex, new Error(resp, ex), httpHeaders, HttpStatus.SERVICE_UNAVAILABLE, request
        );
    }

    @ExceptionHandler(value = {StateNotConsistentException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleNotConsistent(RuntimeException ex, WebRequest request) {
        String resp = "Known resource did not match expectations.";
        return handleExceptionInternal(
                ex, new Error(resp, ex), httpHeaders, HttpStatus.CONFLICT, request
        );
    }

    @ExceptionHandler(value = {DeletionException.class})
    protected ResponseEntity<Object> handleDeletion(RuntimeException ex, WebRequest request) {
        String resp = "Resource could not be removed.";
        return handleExceptionInternal(
                ex, new Error(resp, ex), httpHeaders, HttpStatus.BAD_REQUEST, request
        );
    }

    @ExceptionHandler(value = {SavingException.class})
    protected ResponseEntity<Object> handleSaving(RuntimeException ex, WebRequest request) {
        String resp = "Error during resource saving.";
        return handleExceptionInternal(
                ex, new Error(resp, ex), httpHeaders, HttpStatus.CONFLICT, request
        );
    }
}
