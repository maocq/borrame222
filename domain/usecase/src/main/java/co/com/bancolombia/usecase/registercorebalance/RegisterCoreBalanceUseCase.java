package co.com.bancolombia.usecase.registercorebalance;

import co.com.bancolombia.model.balance.AccountBalance;
import co.com.bancolombia.model.balance.gateways.AccountReconciliationRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class RegisterCoreBalanceUseCase {

    private final AccountReconciliationRepository accountReconciliationRepository;

    public Mono<AccountBalance> execute(AccountBalance balance) {
        return accountReconciliationRepository.update(balance);
    }

    public Flux<AccountBalance> execute(List<AccountBalance> balances) {
          return Flux.fromIterable(balances)
                .flatMap(accountReconciliationRepository::update);
    }
}
