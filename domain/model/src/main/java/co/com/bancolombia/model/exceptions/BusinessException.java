package co.com.bancolombia.model.exceptions;

import co.com.bancolombia.model.exceptions.message.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessErrorMessage errorMessage;
    private final String detail;

    public BusinessException(BusinessErrorMessage errorMessage, String detail) {
        super(errorMessage.getMessage() + ": " + detail);
        this.errorMessage = errorMessage;
        this.detail = detail;
    }
}
