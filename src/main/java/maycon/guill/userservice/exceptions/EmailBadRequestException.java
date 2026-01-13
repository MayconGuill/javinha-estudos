package maycon.guill.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailBadRequestException extends ResponseStatusException {
    public EmailBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public EmailBadRequestException() {
        super(HttpStatus.BAD_REQUEST, "E-mail já existente");
    }
}
