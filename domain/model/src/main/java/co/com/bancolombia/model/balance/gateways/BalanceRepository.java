package co.com.bancolombia.model.balance.gateways;

import co.com.bancolombia.model.balance.Balance;
import reactor.core.publisher.Mono;

public interface BalanceRepository {

    Mono<Balance> update(Balance balance);
}
