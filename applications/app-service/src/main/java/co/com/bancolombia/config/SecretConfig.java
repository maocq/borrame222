package co.com.bancolombia.config;

import co.com.bancolombia.kafkaconsumer.config.KafkaConfigProperties;
import co.com.bancolombia.kafkaconsumer.config.VaultKafkaConfigProperties;
import co.com.bancolombia.secretsmanager.api.GenericManager;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnector;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretConfig {

    @Bean
    public GenericManager getSecretManager(@Value("${aws.region}") String region) {
        return new AWSSecretManagerConnector(region);
    }

    @Bean
    @SneakyThrows
    public KafkaConfigProperties getKafkaConfigProperties(
            GenericManager genericManager, @Value("${aws.strimzi-secret-name}") String secretName) {
        return genericManager.getSecret(secretName, KafkaConfigProperties.class);
    }

    @Bean
    @SneakyThrows
    public VaultKafkaConfigProperties getVaultKafkaConfigProperties(
            GenericManager genericManager, @Value("${aws.vault-kafka-secret-name}") String secretName) {
        return genericManager.getSecret(secretName, VaultKafkaConfigProperties.class);
    }
}
