package com.jivesoftware.android.mobile.sdk.core.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ben.oberkfell on 4/14/14.
 */
public class JiveCoreSearchTypesRequestOptionsTest {

    private final JiveCoreSearchTypesRequestOptions optionsUnderTest = new JiveCoreSearchTypesRequestOptions();

    @Test
    public void testContentTypeFilter() {
        optionsUnderTest.setTypes(ImmutableList.of("document","update","post"));

        Map<String,List<String>> actualQueryParameters = optionsUnderTest.copyQueryParameters();
        Map<String,List<String>> expectedQueryParameters = ImmutableMap.<String, List<String>>of(
                "filter", ImmutableList.of("type(document,update,post)")
        );

        assertEquals(expectedQueryParameters, actualQueryParameters);
    }

}
