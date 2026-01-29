package co.com.bancolombia.model.reconciliationmetrics;

import lombok.Builder;

@Builder(toBuilder = true)
public record ReconciliationMetrics(Long total, Long unreconciledTotal, Long unreconciledTotalContigency) {
}
