package com.jivesoftware.android.mobile.sdk.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jivesoftware.android.mobile.sdk.entity.CodeErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.ObjectErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class ErrorEntityDeserializer extends JsonDeserializer<ErrorEntity> {
    @Nonnull
    private final ObjectMapper objectMapper;

    public ErrorEntityDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Nonnull
    public ErrorEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);
        JsonNode error = node.get("error");
        Class<? extends ErrorEntity> type;
        if (error == null) {
            type = CodeErrorEntity.class;
        } else if (error.isValueNode()) {
            type = SimpleErrorEntity.class;
        } else {
            type = ObjectErrorEntity.class;
        }
        return objectMapper.treeToValue(node, type);
    }
}
