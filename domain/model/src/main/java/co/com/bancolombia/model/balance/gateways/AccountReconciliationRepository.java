package co.com.bancolombia.model.balance.gateways;

import co.com.bancolombia.model.balance.AccountBalance;
import reactor.core.publisher.Mono;

public interface AccountReconciliationRepository {

    Mono<AccountBalance> update(AccountBalance balance);
}
