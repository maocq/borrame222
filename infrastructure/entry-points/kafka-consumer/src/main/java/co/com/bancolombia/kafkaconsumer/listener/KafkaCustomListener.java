package co.com.bancolombia.kafkaconsumer.listener;

import co.com.bancolombia.kafkaconsumer.listener.utils.MapperService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Log4j2
@Component
@AllArgsConstructor
public class KafkaCustomListener {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final MapperService mapper;

    public <T> Mono<Void> process(
            ConsumerRecord<String, byte[]> consumerRecord,
            Function<T, Mono<Void>> handler, Class<T> inputClass, String dlq) {

        return Mono.fromSupplier(() -> mapper.deserialize(consumerRecord.value(), inputClass))
                .flatMap(handler)
                .doOnError(error ->
                        sendDlqMessage(new String(consumerRecord.value(), StandardCharsets.UTF_8), error, dlq))
                .onErrorComplete();
    }

    private void sendDlqMessage(String event, Throwable error, String dlq) {
        log.error("❌ Error kafka consumer ({}) - {}", error.getMessage(), event, error);

        var bytes = mapper.serialize(new DlqError(event, error.getMessage()));
        Mono.fromFuture(() -> kafkaTemplate.send(dlq, bytes))
                .doOnError(errorDlq ->
                        log.error("❌ Error sending Dlq message {}", errorDlq.getMessage(), errorDlq))
                .onErrorComplete()
                .subscribe();
    }

    public record DlqError(String message, String error) {}
}
