package com.jivesoftware.android.mobile.sdk.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.PersonEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class ContentEntityDeserializer extends JsonDeserializer<ContentEntity> {
    @Nonnull
    private final ObjectMapper objectMapper;

    public ContentEntityDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Nullable
    public ContentEntity deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode root = objectMapper.readTree(jp);
        ContentEntity contentEntity = objectMapper.treeToValue(root, ContentEntity.class);
        contentEntity.participants = deserializeStringArrayOrPersonEntityArray(root, "participants");
        contentEntity.users = deserializeStringArrayOrPersonEntityArray(root, "users");
        return contentEntity;
    }

    private Object[] deserializeStringArrayOrPersonEntityArray(JsonNode jsonObject, String jsonArrayName) throws JsonProcessingException {
        JsonNode jsonArray = jsonObject.get(jsonArrayName);
        if (jsonArray != null && jsonArray.isArray()) {
            int size = jsonArray.size();
            if (size > 0) {
                JsonNode firstJsonElement = jsonArray.get(0);
                Object[] array;
                if (firstJsonElement.isValueNode()) {
                    array = new String[size];
                    String firstSelfURL = firstJsonElement.asText();
                    array[0] = firstSelfURL;
                    for (int i = 1, l = jsonArray.size(); i < l; i++) {
                        JsonNode userSelfURLJsonElement = jsonArray.get(i);
                        array[i] = userSelfURLJsonElement.asText();
                    }
                } else if (firstJsonElement.isObject()) {
                    array = new PersonEntity[size];
                    PersonEntity firstPersonEntity = objectMapper.treeToValue(firstJsonElement, PersonEntity.class);
                    array[0] = firstPersonEntity;
                    for (int i = 1, l = jsonArray.size(); i < l; i++) {
                        JsonNode personEntityJsonElement = jsonArray.get(i);
                        PersonEntity personEntity = objectMapper.treeToValue(personEntityJsonElement, PersonEntity.class);
                        array[i] = personEntity;
                    }
                } else {
                    throw new UnexpectedJsonTypeException("Expected Person entity or Person self URL");
                }
                return array;
            } else {
                return new PersonEntity[0];
            }
        }
        return null;
    }
}
