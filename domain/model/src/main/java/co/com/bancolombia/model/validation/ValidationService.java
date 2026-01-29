package co.com.bancolombia.model.validation;

import co.com.bancolombia.model.exceptions.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.DATA_VALIDATION_ERROR;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationService {

    public static <T> void validate(T objeto, Predicate<T> predicate, String error) {
        if (predicate.test(objeto)) {
            throw new BusinessException(DATA_VALIDATION_ERROR, error);
        }
    }

    public static <T> void validate(T objeto, Predicate<T> predicate, Supplier<String> error) {
        if (predicate.test(objeto)) {
            throw new BusinessException(DATA_VALIDATION_ERROR, error.get());
        }
    }


    public static boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }
}