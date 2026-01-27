package co.com.bancolombia.kafkaconsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

@Configuration
public class VaultKafkaConsumerConfig {

    @Bean
    ConsumerFactory<String, byte[]> vaultConsumerFactory(
            VaultKafkaConfigProperties properties, @Value("${adapters.kafka.consumer-group}") String consumerGroup)
            throws UnknownHostException {

        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.servers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config", String.format(
                "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";",
                properties.username(), properties.password()
        ));

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, byte[]>> vaultFactory(
            @Qualifier("vaultConsumerFactory") ConsumerFactory<String, byte[]> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
