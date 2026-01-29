package co.com.bancolombia.kafkaconsumer.dtos;

import co.com.bancolombia.model.balance.AccountBalance;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static co.com.bancolombia.model.balance.AccountBalance.Core.QM;

public record QmResult(Message Message) {

    public static final String BALANCE = "MSTR";

    public AccountBalance getBalance() {
        return new AccountBalance(account(), amount(), dateTime(), QM); //Pendiente
    }

    public String account() {
        return Message.payload.id;
    }

    public BigDecimal amount() {
        return Message.payload.balances.stream()
                .filter(balance -> balance.Type.equals(BALANCE))
                .map(balance -> balance.Amount).findFirst().orElse(BigDecimal.ZERO);
    }

    public ZonedDateTime dateTime() {
        //Message.payload.balancesEventDateTime; //Pendiente
        return ZonedDateTime.now();
    }

    public record Message(
            Payload payload
    ) {}

    public record Payload(
            String id,
            BigDecimal balancesEventDateTime,
            String balancesEventReference,
            String balancesEventCore,
            List<Balance> balances
    ) {}

    public record Balance(
            String Type,
            BigDecimal Amount
    ) {}
}
