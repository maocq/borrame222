package co.com.bancolombia.config.dtos;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "account.filter")
public record AccountFilterDto(
        String excludedStatus, Set<String> excludedSavingsAccounts, Set<String> excludedCheckingAccounts) {
}
