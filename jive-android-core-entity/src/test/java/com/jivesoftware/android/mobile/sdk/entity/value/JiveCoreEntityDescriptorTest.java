package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JiveCoreEntityDescriptorTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void fromString() throws Exception {
        JiveCoreEntityDescriptor entityDescriptor = new JiveCoreEntityDescriptor("1,2");
        assertEquals("1,2", entityDescriptor.toString());
    }

    @Test(expected = NullPointerException.class)
    public void fromString_null() throws Exception {
        new JiveCoreEntityDescriptor(null);
    }

    @Test
    public void fromInts() throws Exception {
        JiveCoreEntityDescriptor entityDescriptor = new JiveCoreEntityDescriptor(1, 2);
        assertEquals("1,2", entityDescriptor.toString());
    }

    @Test
    public void equality() throws Exception {
        JiveCoreEntityDescriptor one = new JiveCoreEntityDescriptor("1,2");
        JiveCoreEntityDescriptor two = new JiveCoreEntityDescriptor(1, 2);
        assertEquals(one, two);
    }

    @Test
    public void deserialization() throws IOException {
        JiveCoreEntityDescriptor actual = objectMapper.readValue("\"1,2\"", JiveCoreEntityDescriptor.class);
        assertEquals("1,2", actual.toString());
    }

}