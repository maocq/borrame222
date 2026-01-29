package co.com.bancolombia.kafkaconsumer.handlers;

import co.com.bancolombia.kafkaconsumer.dtos.BalanceUpdateDto;
import co.com.bancolombia.kafkaconsumer.dtos.PostingResult;
import co.com.bancolombia.kafkaconsumer.dtos.QmResult;
import co.com.bancolombia.kafkaconsumer.dtos.filter.AccountFilter;
import co.com.bancolombia.model.balance.AccountBalance;
import co.com.bancolombia.usecase.registercorebalance.RegisterCoreBalanceUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class EventsHandler {

    private final AccountFilter accountFilter;
    private final RegisterCoreBalanceUseCase registerCoreBalanceUseCase;

    public Mono<Void> updateIseriesBalance(BalanceUpdateDto balanceUpdateDto) {
        if (accountFilter.isInvalid(balanceUpdateDto)) {
            return Mono.empty();
        }

        log.debug("Updating Iseries balance for {}", balanceUpdateDto);
        return registerCoreBalanceUseCase.execute(balanceUpdateDto.getBalance()).then();
    }

    public Mono<Void> updateVaultBalances(PostingResult postingResult) {
        log.debug("Updating Vault balance for {}", postingResult);
        List<AccountBalance> balances = postingResult.committedBalance()
                .stream()
                .map(balance -> new AccountBalance(balance.account_id(), balance.amount(),
                        postingResult.insertionTimestamp(), AccountBalance.Core.VAULT)).toList();

        return registerCoreBalanceUseCase.execute(balances).then();
    }

    public Mono<Void> updateQmBalances(QmResult qmResult) {
        log.debug("Updating QM balance for {}", qmResult);
        return registerCoreBalanceUseCase.execute(qmResult.getBalance()).then();
    }
}
