package com.jivesoftware.android.mobile.sdk.json.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableList;
import com.jivesoftware.android.mobile.sdk.entity.CodeErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.ObjectErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ErrorEntityDeserializerTest {
    private ErrorEntity errorEntity;
    private String json;
    private ObjectMapper objectMapper;

    public ErrorEntityDeserializerTest(ErrorEntity errorEntity, String json) {
        this.errorEntity = errorEntity;
        this.json = json;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return ImmutableList.of(
                new Object[] { new ObjectErrorEntity("Missing comment ID 10308", null, 404), "{\"error\":{\"message\":\"Missing comment ID 10308\",\"status\":404}}" },
                new Object[] { new ObjectErrorEntity("You are not allowed to like or unlike your own content", "contentAuthorCantLike", 409), "{\"error\":{\"message\":\"You are not allowed to like or unlike your own content\",\"code\":\"contentAuthorCantLike\",\"status\":409}}" },
                new Object[] { new SimpleErrorEntity("invalid_client", "The client ID is invalid or not registered: 8rcdqe1a0jx6cuubhddx3znpnszo4ib4.i"), "{\"error\":\"invalid_client\",\"error_description\":\"The client ID is invalid or not registered: 8rcdqe1a0jx6cuubhddx3znpnszo4ib4.i\"}" },
                new Object[] { new SimpleErrorEntity("invalid_grant", "Bad refresh token"), "{\"error\":\"invalid_grant\",\"error_description\":\"Bad refresh token\"}" },
                new Object[] { new CodeErrorEntity(4026, "The request could not be validated as originating from within the SBS application"), "{\"code\" : 4026, \"message\" : \"The request could not be validated as originating from within the SBS application\"}" }
        );
    }

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(ErrorEntity.class, new ErrorEntityDeserializer(objectMapper));
        objectMapper.registerModule(simpleModule);
        this.objectMapper = objectMapper;
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectReader reader = objectMapper.reader(ErrorEntity.class);
        Object actual = reader.readValue(json);
        assertEquals("JSON should be deserialized correctly", errorEntity, actual);
    }
}
