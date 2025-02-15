package serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ContextualInstanceDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        String className = null;
        Object data = null;

        while (p.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = p.currentName();
            p.nextToken();
            if ("@class".equals(fieldName)) {
                className = p.getText();
            } else if ("data".equals(fieldName)) {
                data = mapper.readValue(p, Object.class);
            }
        }

        if (className != null && data != null) {
            try {
                Class<?> clazz = Class.forName(className);
                return mapper.convertValue(data, clazz);
            } catch (ClassNotFoundException e) {
                throw new IOException("Classe non trovata: " + className, e);
            }
        }
        return null;
    }
}