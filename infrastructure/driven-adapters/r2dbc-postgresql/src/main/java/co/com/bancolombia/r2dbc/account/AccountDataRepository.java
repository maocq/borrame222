package co.com.bancolombia.r2dbc.account;

import co.com.bancolombia.r2dbc.write.account.AccountDataDAOWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class AccountDataRepository {

    private final AccountDataDAOWrite accountDataDAOWrite;
    //private final AccountDataDAORead accountDataDAORead; Pendiente - Borrar


    public Mono<String> update(String account, BigDecimal balance, ZonedDateTime dateTime) {
        return accountDataDAOWrite.updateQm(account, balance, dateTime)
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(account);
                    }

                    return accountDataDAOWrite.findById(account)
                            .switchIfEmpty(Mono.defer(() ->
                                    accountDataDAOWrite.insert(AccountData.newQmBalance(account, balance, dateTime))))
                            .map(AccountData::getAccount);
                });
    }
}
