package co.com.bancolombia.r2dbc.read.account;

import co.com.bancolombia.r2dbc.account.AccountData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountDataDAORead extends ReactiveCrudRepository<AccountData, String> {
}

