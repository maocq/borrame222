package co.com.bancolombia.r2dbc.write.account;

import co.com.bancolombia.r2dbc.account.AccountReconciliationData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface AccountReconciliationWriteDao extends ReactiveCrudRepository<AccountReconciliationData, String> {

    @Modifying
    @Query("""
        UPDATE account_reconciliation
            SET iseries_balance = :balance, iseries_datetime = :dateTime, updated_at = NOW()
        WHERE account = :account AND iseries_datetime < :dateTime;
        """)
    Mono<Long> updateIseries(@Param("account") String account, @Param("balance") BigDecimal balance,
                             @Param("dateTime") ZonedDateTime dateTime);

    @Modifying
    @Query("""
        UPDATE account_reconciliation
            SET vault_balance = :balance, vault_datetime = :dateTime, updated_at = NOW()
        WHERE account = :account AND vault_datetime < :dateTime;
        """)
    Mono<Long> updateVault(@Param("account") String account, @Param("balance") BigDecimal balance,
                             @Param("dateTime") ZonedDateTime dateTime);

    @Modifying
    @Query("""
        UPDATE account_reconciliation
            SET qm_balance = :balance, qm_datetime = :dateTime, updated_at = NOW()
        WHERE account = :account AND qm_datetime < :dateTime;
        """)
    Mono<Long> updateQm(@Param("account") String account, @Param("balance") BigDecimal balance,
                           @Param("dateTime") ZonedDateTime dateTime);


    @Query("""
        INSERT INTO account_reconciliation
        (account, iseries_balance, iseries_datetime, vault_balance, vault_datetime, qm_balance, qm_datetime, updated_at)
        VALUES(:#{#data.account}, :#{#data.iseriesBalance}, :#{#data.iseriesDatetime}, :#{#data.vaultBalance}, 
               :#{#data.vaultDatetime}, :#{#data.qmBalance}, :#{#data.qmDatetime}, :#{#data.updatedAt})
        ON CONFLICT (account)
        DO UPDATE SET
            iseries_balance = :#{#data.iseriesBalance}, iseries_datetime = :#{#data.iseriesDatetime}
        RETURNING *;
        """)
    Mono<AccountReconciliationData> insertIseries(@Param("data") AccountReconciliationData data);

    @Query("""
        INSERT INTO account_reconciliation
        (account, iseries_balance, iseries_datetime, vault_balance, vault_datetime, qm_balance, qm_datetime, updated_at)
        VALUES(:#{#data.account}, :#{#data.iseriesBalance}, :#{#data.iseriesDatetime}, :#{#data.vaultBalance}, 
               :#{#data.vaultDatetime}, :#{#data.qmBalance}, :#{#data.qmDatetime}, :#{#data.updatedAt})
        ON CONFLICT (account)
        DO UPDATE SET
            vault_balance = :#{#data.vaultBalance}, vault_datetime = :#{#data.vaultDatetime}
        RETURNING *;
        """)
    Mono<AccountReconciliationData> insertVault(@Param("data") AccountReconciliationData data);

    @Query("""
        INSERT INTO account_reconciliation
        (account, iseries_balance, iseries_datetime, vault_balance, vault_datetime, qm_balance, qm_datetime, updated_at)
        VALUES(:#{#data.account}, :#{#data.iseriesBalance}, :#{#data.iseriesDatetime}, :#{#data.vaultBalance}, 
               :#{#data.vaultDatetime}, :#{#data.qmBalance}, :#{#data.qmDatetime}, :#{#data.updatedAt})
        ON CONFLICT (account)
        DO UPDATE SET
            qm_balance = :#{#data.qmBalance}, qm_datetime = :#{#data.qmDatetime}
        RETURNING *;
        """)
    Mono<AccountReconciliationData> insertQm(@Param("data") AccountReconciliationData data);
}

