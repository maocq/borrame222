package co.com.bancolombia.kafkaconsumer;

import co.com.bancolombia.kafkaconsumer.dtos.BalanceUpdateDto;
import co.com.bancolombia.kafkaconsumer.dtos.PostingResult;
import co.com.bancolombia.kafkaconsumer.handlers.EventsHandler;
import co.com.bancolombia.kafkaconsumer.listener.KafkaCustomListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaConsumer {

    private final EventsHandler eventsHandler;
    private final KafkaCustomListener listener;
    private final String dlq;

    public KafkaConsumer(EventsHandler eventsHandler, KafkaCustomListener listener,
                         @Value("${adapters.kafka.topics.dlq-topic}") String dlq) {
        this.eventsHandler = eventsHandler;
        this.listener = listener;
        this.dlq = dlq;
    }

    @KafkaListener(topics = "${adapters.kafka.topics.iseries-topic}", containerFactory = "factory")
    public Mono<Void> listenIseriesBalance(ConsumerRecord<String, byte[]> consumerRecord) {
        return listener.process(consumerRecord, eventsHandler::updateIseriesBalance, BalanceUpdateDto.class, dlq);
    }

    @KafkaListener(
            topics = "${adapters.kafka.topics.vault-topic}", containerFactory = "vaultFactory")
    public Mono<Void> listenVaultBalances(ConsumerRecord<String, byte[]> consumerRecord) {
        return listener.process(consumerRecord, eventsHandler::updateVaultBalances, PostingResult.class, dlq);
    }
}
