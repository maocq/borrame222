package co.com.bancolombia.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalErrorMessage {
    TECHNICAL_RESTCLIENT_ERROR("AUT0001","Technical error rest client");

    private final String code;
    private final String message;
}
