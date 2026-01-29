package co.com.bancolombia.r2dbc.account;

import co.com.bancolombia.model.balance.AccountBalance;
import co.com.bancolombia.model.balance.gateways.AccountReconciliationRepository;
import co.com.bancolombia.model.reconciliationmetrics.ReconciliationMetrics;
import co.com.bancolombia.r2dbc.read.account.AccountReconciliationReadDao;
import co.com.bancolombia.r2dbc.write.account.AccountReconciliationWriteDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountReconciliationDataRepository implements AccountReconciliationRepository {

    private final AccountReconciliationWriteDao accountReconciliationWriteDao;
    private final AccountReconciliationReadDao accountReconciliationReadDao;

    @Override
    @Transactional
    public Mono<ReconciliationMetrics> metrics(String id) {
        return accountReconciliationReadDao.tryLock()
                .flatMap(lock -> lock ? accountReconciliationReadDao.metrics() : Mono.empty());
    }

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
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(balance);
                    }
                    return accountReconciliationWriteDao.findById(balance.account())
                            .switchIfEmpty(Mono.defer(() -> accountReconciliationWriteDao
                                    .insertIseries(AccountReconciliationData.newBalance(balance))))
                            .map(accountReconciliationData ->  balance);
                });
    }

    private Mono<AccountBalance> updateVault(AccountBalance balance) {
        return accountReconciliationWriteDao.updateVault(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(balance);
                    }
                    return accountReconciliationWriteDao.findById(balance.account())
                            .switchIfEmpty(Mono.defer(() -> accountReconciliationWriteDao
                                    .insertVault(AccountReconciliationData.newBalance(balance))))
                            .map(accountReconciliationData ->  balance);
                });
    }

    private Mono<AccountBalance> updateQm(AccountBalance balance) {
        return accountReconciliationWriteDao.updateQm(balance.account(), balance.balance(), balance.dateTime())
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(balance);
                    }
                    return accountReconciliationWriteDao.findById(balance.account())
                            .switchIfEmpty(Mono.defer(() -> accountReconciliationWriteDao
                                    .insertQm(AccountReconciliationData.newBalance(balance))))
                            .map(accountReconciliationData ->  balance);
                });
    }
}
