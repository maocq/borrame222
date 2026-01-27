package co.com.bancolombia.kafkaconsumer.handlers;

import co.com.bancolombia.kafkaconsumer.dtos.IseriesBalance;
import co.com.bancolombia.kafkaconsumer.dtos.PostingResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class EventsHandler {

    public Mono<Void> updateIseriesBalance(IseriesBalance iseriesBalance) {
        System.out.println("Processing IseriesBalance: " + iseriesBalance);
        return Mono.empty();
    }

    public Mono<Void> updateVaultBalances(PostingResult postingResult) {
        System.out.println("Processing PostingResult: " + postingResult);
        return Mono.empty();
    }
}
