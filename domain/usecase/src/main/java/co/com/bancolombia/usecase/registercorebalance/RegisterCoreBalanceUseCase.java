package co.com.bancolombia.usecase.registercorebalance;

import co.com.bancolombia.model.balance.Balance;
import co.com.bancolombia.model.balance.gateways.BalanceRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class RegisterCoreBalanceUseCase {

    private final BalanceRepository balanceRepository;

    public Mono<Balance> execute(Balance balance) {
        return balanceRepository.update(balance);
    }

    public Flux<Balance> execute(List<Balance> balances) {
          return Flux.fromIterable(balances)
                .flatMap(balanceRepository::update);
    }
}
