package co.com.bancolombia.kafkaconsumer.dtos.filter;

import java.util.Set;

public record AccountFilterConfig(
        String excludedStatus, Set<String> excludedSavingsAccounts, Set<String> excludedCheckingAccounts) {
}
