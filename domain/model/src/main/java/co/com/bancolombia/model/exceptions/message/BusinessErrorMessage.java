package co.com.bancolombia.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorMessage {
    DATA_VALIDATION_ERROR("AUB0001", "Validation data error");

    private final String code;
    private final String message;
}
