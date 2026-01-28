package co.com.bancolombia.kafkaconsumer.handlers;

import co.com.bancolombia.kafkaconsumer.dtos.BalanceUpdateDto;
import co.com.bancolombia.kafkaconsumer.dtos.PostingResult;
import co.com.bancolombia.kafkaconsumer.dtos.filter.AccountFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class EventsHandler {

    private final AccountFilter accountFilter;

    public Mono<Void> updateIseriesBalance(BalanceUpdateDto balanceUpdateDto) {
        if (accountFilter.isInvalid(balanceUpdateDto)) {
            return Mono.empty();
        }

        System.out.println("Processing IseriesBalance: " + balanceUpdateDto);
        return Mono.empty();
    }

    public Mono<Void> updateVaultBalances(PostingResult postingResult) {
        System.out.println("Processing PostingResult: " + postingResult);
        return Mono.empty();
    }
}
