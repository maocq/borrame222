package co.com.bancolombia.model.balance.gateways;

import co.com.bancolombia.model.balance.AccountBalance;
import co.com.bancolombia.model.reconciliationmetrics.ReconciliationMetrics;
import reactor.core.publisher.Mono;

public interface AccountReconciliationRepository {

    Mono<AccountBalance> update(AccountBalance balance);
    Mono<ReconciliationMetrics> metrics(String id);
}
