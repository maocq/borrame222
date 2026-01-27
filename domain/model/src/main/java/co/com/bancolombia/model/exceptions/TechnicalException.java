package co.com.bancolombia.model.exceptions;

import co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage;
import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException {
    private final TechnicalErrorMessage errorMessage;
    private final String detail;

    public TechnicalException(TechnicalErrorMessage errorMessage, String detail) {
        super(errorMessage.getMessage() + ": " + detail);
        this.errorMessage = errorMessage;
        this.detail = detail;
    }

    public TechnicalException(TechnicalErrorMessage errorMessage, String detail, Throwable cause) {
        super(errorMessage.getMessage() + ": " + detail, cause);
        this.errorMessage = errorMessage;
        this.detail = detail;
    }
}