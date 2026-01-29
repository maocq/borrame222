package co.com.bancolombia.kafkaconsumer.dtos;

import co.com.bancolombia.model.balance.AccountBalance;
import co.com.bancolombia.model.validation.ValidationService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static co.com.bancolombia.model.balance.AccountBalance.Core.QM;
import static co.com.bancolombia.model.validation.ValidationService.validate;

public record QmResult(Message Message) {
    public static final String BALANCE = "MSTR";

    public AccountBalance getBalance() {
        return new AccountBalance(account(), amount(), dateTime(), QM);
    }

    private String account() {
        return Message.payload.id;
    }

    private BigDecimal amount() {
        return Message.payload.balances.stream()
                .filter(balance -> balance.Type.equals(BALANCE))
                .map(balance -> balance.Amount).findFirst().orElse(BigDecimal.ZERO);
    }

    private ZonedDateTime dateTime() {
        return Message.payload.balancesEventDateTime;
    }

    public QmResult {
        validate(Message, Objects::isNull, "Invalid Message");
    }


    public record Message(
            Payload payload
    ) {
        public Message {
            validate(payload, Objects::isNull, "Invalid payload");
        }
    }

    public record Payload(
            String id,
            ZonedDateTime balancesEventDateTime,
            String balancesEventReference,
            String balancesEventCore,
            List<Balance> balances
    ) {
        public Payload {
            validate(id, ValidationService::isBlankString, "Invalid Payload id");
            validate(balancesEventDateTime, Objects::isNull, "Invalid balancesEventDateTime");
            validate(balances, Objects::isNull, "Invalid balances");
        }
    }

    public record Balance(
            String Type,
            BigDecimal Amount
    ) {
        public Balance {
            validate(Type, ValidationService::isBlankString, "Invalid Balance Type");
            validate(Amount, Objects::isNull, "Invalid Balance Amount");
        }
    }
}
