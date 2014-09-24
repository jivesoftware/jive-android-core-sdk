package com.jivesoftware.android.mobile.sdk.core;

import com.jayway.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

public class AbstractDelayedRestITest extends AbstractITest {
    // for REST calls that don't synchronously update, so we have to wait for them to propagate.
    protected static final long DELAYED_TIMEOUT_AMOUNT = TIMEOUT_AMOUNT * 5;
    protected static final TimeUnit DELAYED_TIMEOUT_TIME_UNIT = TIMEOUT_TIME_UNIT;

    @Before
    public void setup() throws Exception {
        super.setup();

        Awaitility.setDefaultTimeout(DELAYED_TIMEOUT_AMOUNT, DELAYED_TIMEOUT_TIME_UNIT);
    }

    @After
    public void tearDown() throws Exception {
        Awaitility.setDefaultTimeout(TIMEOUT_AMOUNT, TIMEOUT_TIME_UNIT);

        super.tearDown();
    }
}
