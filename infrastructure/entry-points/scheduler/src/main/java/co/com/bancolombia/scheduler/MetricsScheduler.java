package co.com.bancolombia.scheduler;

import co.com.bancolombia.model.balance.gateways.AccountReconciliationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.util.logging.Level.SEVERE;

@Log
@Component
@EnableScheduling
@AllArgsConstructor
public class MetricsScheduler {
    public static final String METRICS = "METRICS";

    private final AccountReconciliationRepository accountReconciliationRepository;

    @Scheduled(cron = "${batch.cron}")
    public void run() {
        accountReconciliationRepository.metrics(METRICS)
                .doOnNext(metrics -> log.info("📊 Metrics: " + metrics))
                .onErrorContinue((error, event) ->
                        log.log(SEVERE, "❌ Error retrieving metrics " + error.getMessage(), error)).subscribe();
    }
}
