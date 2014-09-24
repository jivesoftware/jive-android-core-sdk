package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.mobile.matcher.MapMatchers;
import com.jivesoftware.android.mobile.sdk.util.JiveURLUtil;
import org.hamcrest.Matchers;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.jivesoftware.android.mobile.matcher.IterableMatchers.containsAll;
import static com.jivesoftware.android.mobile.matcher.MapMatchers.values;
import static org.junit.Assert.assertThat;

public abstract class TestEndpoint {
    private static final Properties PROPERTIES;

    static {
        try {
            PROPERTIES = createProperties("TestEndpoint.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("TestEndpoint.properties is not included in git for security reasons create one with TestEndpoint.example.properties", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Properties exampleProperties;
        try {
            exampleProperties = createProperties("TestEndpoint.example.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertThat(exampleProperties, Matchers.<Map<Object, Object>>allOf(
                MapMatchers.keySet(Matchers.<Set<?>>equalTo(PROPERTIES.keySet())),
                values(containsAll(Matchers.<Object>equalTo("")))));
    }

    @Nonnull
    private static Properties createProperties(@Nonnull String filename) throws IOException {
        Properties properties = new Properties();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("test-data/" + filename);
            properties.load(fileInputStream);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return properties;
    }

    @Nonnull
    private static String getProperty(@Nonnull String key) {
        String value = PROPERTIES.getProperty(key);
        if ((value == null) || (value.length() == 0)) {
            throw new AssertionError("Missing property \"" + key + "\" in TestEndpoint.properties");
        } else {
            return value;
        }
    }

    // TEST_URL must be defined above TestUser because TestUser uses it in its constructor.
    public static final URL TEST_URL = JiveURLUtil.createURL(getProperty("TEST_URL"));

    protected static final long TIMEOUT_AMOUNT = 10L;
    protected static final TimeUnit TIMEOUT_TIME_UNIT = TimeUnit.SECONDS;

    public static final TestUser ADMIN = TestUser.ADMIN;
    public static final TestUser USER2 = TestUser.USER2;
    public static final TestUser USER3 = TestUser.USER3;

    public static String toPathAndQuery(URL url) {
        return url.toExternalForm().substring(TEST_URL.toExternalForm().length());
    }

    public enum TestUser {
        ADMIN(
                getProperty("TestUser.ADMIN.displayName"),
                getProperty("TestUser.ADMIN.username"),
                getProperty("TestUser.ADMIN.password"),
                getProperty("TestUser.ADMIN.selfURL")),
        USER2(
                getProperty("TestUser.USER2.displayName"),
                getProperty("TestUser.USER2.username"),
                getProperty("TestUser.USER2.password"),
                getProperty("TestUser.USER2.selfURL")),
        USER3(
                getProperty("TestUser.USER3.displayName"),
                getProperty("TestUser.USER3.username"),
                getProperty("TestUser.USER3.password"),
                getProperty("TestUser.USER3.selfURL"));

        @Nonnull
        public final String displayName;
        @Nonnull
        public final String username;
        @Nonnull
        public final String password;
        @Nonnull
        public final URL selfURL;
        @Nonnull
        public final String selfPathAndQuery;

        TestUser(@Nonnull String displayName, @Nonnull String username, @Nonnull String password, @Nonnull String selfUrl) {
            this.displayName = displayName;
            this.username = username;
            this.password = password;
            this.selfURL = JiveURLUtil.createURL(selfUrl);
            this.selfPathAndQuery = toPathAndQuery(selfURL);
        }
    }
}
