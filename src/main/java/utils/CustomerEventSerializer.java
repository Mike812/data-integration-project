package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import company.CustomerEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class CustomerEventSerializer implements Serializer<CustomerEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, CustomerEvent customerEvent) {
        try {
            if (customerEvent == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(customerEvent);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing CustomerEvent to byte[]");
        }
    }
}