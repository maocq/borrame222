package co.com.bancolombia.r2dbc.account;

import co.com.bancolombia.model.balance.AccountBalance;
import co.com.bancolombia.model.balance.gateways.AccountReconciliationRepository;
import co.com.bancolombia.r2dbc.write.account.AccountReconciliationWriteDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountReconciliationDataRepository implements AccountReconciliationRepository {

    private final AccountReconciliationWriteDao accountReconciliationWriteDao;
    //private final AccountDataDAORead accountDataDAORead; Pendiente - Borrar


    @Override
    public Mono<AccountBalance> update(AccountBalance balance) {
        return switch (balance.core()) {
            case ISERIES -> updateIseries(balance);
            case VAULT -> updateVault(balance);
            case QM -> updateQm(balance);
        };
    }


    private Mono<AccountBalance> updateIseries(AccountBalance balance) {
        return accountReconciliationWriteDao.updateIseries(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> insertIfNotExist(rowsUpdated, balance));
    }

    private Mono<AccountBalance> updateVault(AccountBalance balance) {
        return accountReconciliationWriteDao.updateVault(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> insertIfNotExist(rowsUpdated, balance));
    }

    private Mono<AccountBalance> updateQm(AccountBalance balance) {
        return accountReconciliationWriteDao.updateQm(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> insertIfNotExist(rowsUpdated, balance));
    }

    private Mono<AccountBalance> insertIfNotExist(Long rowsUpdated, AccountBalance balance) {
        if (rowsUpdated > 0) {
            return Mono.just(balance);
        }

        return accountReconciliationWriteDao.findById(balance.account())
                .switchIfEmpty(Mono.defer(() -> accountReconciliationWriteDao.insert(AccountReconciliationData.newBalance(balance))))
                .map(accountReconciliationData ->  balance);
    }
}
