package com.jivesoftware.android.mobile.sdk.entity;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ErrorEntitySerializationTest {

    @Test
    public void serializeCodeErrorEntity() throws Exception {
        CodeErrorEntity testObject = serializeAndDeserialize(CodeErrorEntity.class, new CodeErrorEntity(42, "message"));

        assertEquals((Integer)42, testObject.getErrorCode());
        assertEquals("message", testObject.getDescription());
        assertEquals("42", testObject.getAPIErrorCode());
    }

    @Test
    public void serializeSimpleErrorEntity() throws Exception {
        SimpleErrorEntity testObject = serializeAndDeserialize(SimpleErrorEntity.class, new SimpleErrorEntity("INVALID_REQUEST", "description"));

        assertEquals((Integer)SimpleErrorEntity.OAuth2ErrorType.INVALID_REQUEST.ordinal(), testObject.getErrorCode());
        assertEquals("description", testObject.getDescription());
        assertEquals("INVALID_REQUEST", testObject.getAPIErrorCode());
    }

    @Test
    public void serializeObjectErrorEntity() throws Exception {
        ObjectErrorEntity testObject = serializeAndDeserialize(ObjectErrorEntity.class, new ObjectErrorEntity("message", "code", 42));

        assertEquals((Integer)42, testObject.getErrorCode());
        assertEquals("message", testObject.getDescription());
        assertEquals("code", testObject.getAPIErrorCode());
    }

    @Nonnull
    private static <S extends Serializable> S serializeAndDeserialize(@Nonnull Class<S> clazz, @Nonnull S serializable) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
        }

        S deserializedSerializable;
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            deserializedSerializable = clazz.cast(objectInputStream.readObject());
            objectInputStream.close();
        }

        assertNotNull(deserializedSerializable);
        return deserializedSerializable;
    }
}
