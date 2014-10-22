package com.jivesoftware.android.mobile.sdk.entity.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(Parameterized.class)
public class JiveCoreValueEnumDeserializationTest<E extends Enum<E>, I> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<E> enumClass;
    private final Class<I> interfaceClass;

    public JiveCoreValueEnumDeserializationTest(Class<E> enumClass, Class<I> interfaceClass) {
        this.enumClass = enumClass;
        this.interfaceClass = interfaceClass;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object[]> result = new ArrayList<Object[]>();
        result.add(new Object[] { JiveCoreContentType.class, JiveCoreContentTypeValue.class });
        result.add(new Object[] { JiveCoreDirective.class, JiveCoreDirectiveValue.class });
        result.add(new Object[] { JiveCoreEntryState.class, JiveCoreEntryStateValue.class });
        result.add(new Object[] { JiveCoreOutcomeType.class, JiveCoreOutcomeTypeValue.class });
        result.add(new Object[] { JiveCorePlaceType.class, JiveCorePlaceTypeValue.class });
        result.add(new Object[] { JiveCoreRelationship.class, JiveCoreRelationshipValue.class });
        result.add(new Object[] { JiveCoreSort.class, JiveCoreSortValue.class });
        result.add(new Object[] { JiveCoreStatus.class, JiveCoreStatusValue.class });
        result.add(new Object[] { JiveCoreVerb.class, JiveCoreVerbValue.class });
        return result;
    }

    @Test
    public void toEnum() throws IOException {
        E[] enumConstants = enumClass.getEnumConstants();
        Enum<E> expected = enumConstants[0];
        I actual = objectMapper.readValue('"' + expected.toString() + '"', interfaceClass);
        assertSame(actual, expected);
    }

    @Test
    public void toNonEnum() throws IOException {
        String expected = "custom value not in enum";
        I value = objectMapper.readValue('"' + expected + '"', interfaceClass);
        assertFalse(enumClass.isAssignableFrom(value.getClass()));
        assertEquals(expected, value.toString());
    }

    @Test
    public void fromNull() throws IOException {
        I value = objectMapper.readValue("null", interfaceClass);
        assertNull(value);
    }

}
