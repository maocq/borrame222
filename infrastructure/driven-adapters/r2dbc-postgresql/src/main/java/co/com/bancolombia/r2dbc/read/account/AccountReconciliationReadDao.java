package co.com.bancolombia.r2dbc.read.account;

import co.com.bancolombia.model.reconciliationmetrics.ReconciliationMetrics;
import co.com.bancolombia.r2dbc.account.AccountReconciliationData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AccountReconciliationReadDao extends ReactiveCrudRepository<AccountReconciliationData, String> {

    @Query("""
        SELECT
          COUNT(1) AS total,
          COUNT(1) FILTER (
            WHERE iseries_balance <> vault_balance
               OR iseries_balance <> qm_balance
               OR vault_balance <> qm_balance
          ) AS unreconciled_total,
          COUNT(1) FILTER (
            WHERE vault_balance <> qm_balance
          ) AS unreconciled_total_contigency
        FROM account_reconciliation;
        """)
    Mono<ReconciliationMetrics> metrics();


    @Query("SELECT pg_try_advisory_xact_lock(:id)")
    Mono<Boolean> tryLock(@Param("id") int id);
}

