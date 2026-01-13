package maycon.guill.userservice.handlers;

import maycon.guill.userservice.exceptions.EmailBadRequestException;
import maycon.guill.userservice.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalHandlersExceptions {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GlobalMessage> handleUserNotFoundException(UserNotFoundException e) {
        var message = new GlobalMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GlobalMessage> handleNoResourceFoundException(NoResourceFoundException e) {
        var message = new GlobalMessage(HttpStatus.NOT_FOUND.value(), "Rota não encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<GlobalMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        var message = new GlobalMessage(HttpStatus.BAD_GATEWAY.value(), "Erro durante a inserção no campo. Campo já existente.");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(message);
    }

    @ExceptionHandler(EmailBadRequestException.class)
    public ResponseEntity<GlobalMessage> handleEmailBadRequestException(EmailBadRequestException e) {
        var message = new GlobalMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
