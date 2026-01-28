package co.com.bancolombia.config;

import co.com.bancolombia.config.dtos.AccountFilterDto;
import co.com.bancolombia.kafkaconsumer.dtos.filter.AccountFilterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public AccountFilterConfig accountFilterConfig(AccountFilterDto dto) {
        return new AccountFilterConfig(
                dto.excludedStatus(), dto.excludedSavingsAccounts(), dto.excludedCheckingAccounts());
    }
}
