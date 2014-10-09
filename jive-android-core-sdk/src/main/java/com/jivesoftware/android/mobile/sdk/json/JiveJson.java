package com.jivesoftware.android.mobile.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.json.deserializer.ContentEntityDeserializer;
import com.jivesoftware.android.mobile.sdk.json.deserializer.ErrorEntityDeserializer;
import com.jivesoftware.android.mobile.sdk.util.DateFormatUtil;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class JiveJson {

    @Nonnull
    private final ObjectMapper objectMapper;

    public JiveJson() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DateFormatUtil.getGmtIso8601DateFormat());

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ErrorEntity.class, new ErrorEntityDeserializer(objectMapper));
        simpleModule.setDeserializerModifier(new BeanDeserializerModifier() {
            ContentEntityDeserializer contentEntityDeserializer = new ContentEntityDeserializer(new ObjectMapper());

            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (beanDesc.getBeanClass().equals(ContentEntity.class)) {
                    return contentEntityDeserializer;
                }
                return deserializer;
            }
        });
        objectMapper.registerModule(simpleModule);

        this.objectMapper = objectMapper;
    }

    public <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public <T> T fromJsonElement(JsonNode jsonElement, Class<T> clazz) throws IOException {
        ObjectReader reader = objectMapper.reader(clazz);
        return reader.readValue(jsonElement);
    }

    public <T> T fromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        JiveJsonInputStream jiveJsonInputStream = new JiveJsonInputStream(inputStream);
        return objectMapper.readValue(jiveJsonInputStream, clazz);
    }

    public <T> T fromJson(Reader reader, Class<T> clazz) throws IOException {
        return objectMapper.readValue(reader, clazz);
    }

    public String toJson(Object src) {
        try {
            return objectMapper.writer().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("This shouldn't be possible because we're writing to memory", e);
        }
    }
}
