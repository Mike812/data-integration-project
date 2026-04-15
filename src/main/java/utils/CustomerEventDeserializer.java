package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.CustomerEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomerEventDeserializer implements Deserializer<CustomerEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CustomerEvent deserialize(String s, byte[] bytes) {
        try {
            if (bytes == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(bytes, "UTF-8"), CustomerEvent.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to CustomerEvent");
        }
    }
}