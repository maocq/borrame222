package co.com.bancolombia.kafkaconsumer.listener.utils;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class MapperService {

    private final ObjectMapper mapper;

    @SneakyThrows
    public <T> T deserialize(byte[] data, Class<T> valueType) {
        return mapper.readValue(data, valueType);
    }

    @SneakyThrows
    public <T> byte[] serialize(T data) {
        return mapper.writeValueAsBytes(data);
    }
}
