package co.com.bancolombia.r2dbc.account;

import co.com.bancolombia.model.balance.Balance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("account_reconciliation")
public class AccountData {
    @Id
    private String account;
    private BigDecimal iseriesBalance;
    private ZonedDateTime iseriesDatetime;
    private BigDecimal vaultBalance;
    private ZonedDateTime vaultDatetime;
    private BigDecimal qmBalance;
    private ZonedDateTime qmDatetime;
    private ZonedDateTime updatedAt;

    public static AccountData newBalance(Balance balance) {
        return switch (balance.core()) {
            case ISERIES -> newIseriesBalance(balance);
            case VAULT -> newVaultBalance(balance);
            case QM -> newQmBalance(balance);
        };
    }

    private static AccountData newIseriesBalance(Balance balance) {
        var dateTimeByDefault = ZonedDateTime.now().minusYears(1);
        return new AccountData(balance.account(), balance.balance(), balance.dateTime(), BigDecimal.ZERO,
                dateTimeByDefault, BigDecimal.ZERO, dateTimeByDefault, ZonedDateTime.now());
    }

    private static AccountData newVaultBalance(Balance balance) {
        var dateTimeByDefault = ZonedDateTime.now().minusYears(1);
        return new AccountData(balance.account(), BigDecimal.ZERO, dateTimeByDefault, balance.balance(),
                balance.dateTime(), BigDecimal.ZERO, dateTimeByDefault, ZonedDateTime.now());
    }

    private static AccountData newQmBalance(Balance balance) {
        var dateTimeByDefault = ZonedDateTime.now().minusYears(1);
        return new AccountData(balance.account(), BigDecimal.ZERO, dateTimeByDefault, BigDecimal.ZERO,
                dateTimeByDefault, balance.balance(), balance.dateTime(), ZonedDateTime.now());
    }
}
