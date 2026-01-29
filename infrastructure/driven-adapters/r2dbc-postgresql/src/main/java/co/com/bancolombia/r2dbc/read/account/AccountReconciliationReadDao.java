package co.com.bancolombia.r2dbc.read.account;

import co.com.bancolombia.r2dbc.account.AccountReconciliationData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountReconciliationReadDao extends ReactiveCrudRepository<AccountReconciliationData, String> {
}

