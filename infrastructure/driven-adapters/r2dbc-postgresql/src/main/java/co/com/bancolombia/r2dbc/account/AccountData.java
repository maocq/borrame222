package co.com.bancolombia.r2dbc.account;

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

    public static AccountData newIseriesBalance(String account, BigDecimal balance, ZonedDateTime dateTime) {
        var dateTimeByDefault = ZonedDateTime.now().minusYears(1);
        return new AccountData(account, balance, dateTime, BigDecimal.ZERO,
                dateTimeByDefault, BigDecimal.ZERO, dateTimeByDefault, ZonedDateTime.now());
    }

    public static AccountData newVaultBalance(String account, BigDecimal balance, ZonedDateTime dateTime) {
        var dateTimeByDefault = ZonedDateTime.now().minusYears(1);
        return new AccountData(account, BigDecimal.ZERO, dateTimeByDefault, balance,
                dateTime, BigDecimal.ZERO, dateTimeByDefault, ZonedDateTime.now());
    }

    public static AccountData newQmBalance(String account, BigDecimal balance, ZonedDateTime dateTime) {
        var dateTimeByDefault = ZonedDateTime.now().minusYears(1);
        return new AccountData(account, BigDecimal.ZERO, dateTimeByDefault, BigDecimal.ZERO,
                dateTimeByDefault, balance, dateTime, ZonedDateTime.now());
    }
}
