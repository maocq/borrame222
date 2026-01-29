package co.com.bancolombia.r2dbc.account;

import co.com.bancolombia.model.balance.Balance;
import co.com.bancolombia.model.balance.gateways.BalanceRepository;
import co.com.bancolombia.r2dbc.write.account.AccountDataDAOWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountDataRepository implements BalanceRepository {

    private final AccountDataDAOWrite accountDataDAOWrite;
    //private final AccountDataDAORead accountDataDAORead; Pendiente - Borrar


    @Override
    public Mono<Balance> update(Balance balance) {
        return switch (balance.core()) {
            case ISERIES -> updateIseries(balance);
            case VAULT -> updateVault(balance);
            case QM -> updateQm(balance);
        };
    }


    private Mono<Balance> updateIseries(Balance balance) {
        return accountDataDAOWrite.updateIseries(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> insertIfNotExist(rowsUpdated, balance));
    }

    private Mono<Balance> updateVault(Balance balance) {
        return accountDataDAOWrite.updateVault(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> insertIfNotExist(rowsUpdated, balance));
    }

    private Mono<Balance> updateQm(Balance balance) {
        return accountDataDAOWrite.updateQm(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> insertIfNotExist(rowsUpdated, balance));
    }

    private Mono<Balance> insertIfNotExist(Long rowsUpdated, Balance balance) {
        if (rowsUpdated > 0) {
            return Mono.just(balance);
        }

        return accountDataDAOWrite.findById(balance.account())
                .switchIfEmpty(Mono.defer(() -> accountDataDAOWrite.insert(AccountData.newBalance(balance))))
                .map(accountData ->  balance);
    }
}
