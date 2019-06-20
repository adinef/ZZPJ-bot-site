package pl.lodz.p.it.zzpj.botsite.config.errorhandling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.deletion.DeletionException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.notfound.NotFoundException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.retrieval.RetrievalTimeException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.saving.SavingException;
import pl.lodz.p.it.zzpj.botsite.exceptions.entity.unconsistent.StateNotConsistentException;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler{

    private final HttpHeaders httpHeaders;

    @Autowired
    public RestResponseEntityExceptionHandler(@Qualifier("json") HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Error> handleNotFound(Exception ex, WebRequest request) {
        String resp = "Resource not found.";
        return handleExceptionInternal(
                new Error(resp, ex), HttpStatus.NOT_FOUND, httpHeaders
        );
    }
    @ExceptionHandler(value = {RetrievalTimeException.class})
    protected ResponseEntity<Error> handleCouldNotRetrieve(Exception ex, WebRequest request) {
        String resp = "Resource could not be retrieved.";
        return handleExceptionInternal(
                new Error(resp, ex), HttpStatus.NOT_FOUND, httpHeaders
        );
    }

    @ExceptionHandler(value = {StateNotConsistentException.class, IllegalArgumentException.class})
    protected ResponseEntity<Error> handleNotConsistent(Exception ex, WebRequest request) {
        String resp = "Known resource did not match expectations.";
        return handleExceptionInternal(
                new Error(resp, ex), HttpStatus.NOT_FOUND, httpHeaders
        );
    }

    @ExceptionHandler(value = {DeletionException.class})
    protected ResponseEntity<Error> handleDeletion(Exception ex, WebRequest request) {
        String resp = "Resource could not be removed.";
        return handleExceptionInternal(
                new Error(resp, ex), HttpStatus.NOT_FOUND, httpHeaders
        );
    }

    @ExceptionHandler(value = {SavingException.class})
    protected ResponseEntity<Error> handleSaving(Exception ex, WebRequest request) {
        String resp = "Error during resource saving.";
        return handleExceptionInternal(
                new Error(resp, ex), HttpStatus.NOT_FOUND, httpHeaders
        );
    }

    private ResponseEntity<Error> handleExceptionInternal(Error error, HttpStatus status, HttpHeaders httpHeaders) {
        return (ResponseEntity<Error>) new ResponseEntity(error, httpHeaders, status);
    }
}
