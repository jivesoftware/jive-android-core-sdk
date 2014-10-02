package com.jivesoftware.android.mobile.sdk.core;

import org.junit.Test;

public class JiveCoreMissionITest extends AbstractITest {
    @Test
    public void completeMission() throws Exception {
        jiveCoreAdmin.completeMission("FORM_LOGIN").call();
        // only test is that no exception is thrown
    }
}
