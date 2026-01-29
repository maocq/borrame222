package co.com.bancolombia.model.balance;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder(toBuilder = true)
public record Balance(
        String account,
        BigDecimal balance,
        ZonedDateTime dateTime,
        Core core
) {

    public enum Core { ISERIES, VAULT, QM }
}
