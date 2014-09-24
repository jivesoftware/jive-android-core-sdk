package com.jivesoftware.android.mobile.sdk.httpclient;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreConstants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mark.schisler on 8/18/14.
 */
public class JiveCoreAuthenticationHandler extends DefaultTargetAuthenticationHandler {

    // Copied from AbstractAuthenticationHandler to avoid lots of redundant object creation
    private static final List<String> DEFAULT_SCHEME_PRIORITY =
            Collections.unmodifiableList(Arrays.asList(
                    "ntlm",
                    "digest",
                    "basic",
                    JiveCoreConstants.JIVE_CORE_AUTH_SCHEME_NAME
            ));

    @Override
    protected List<String> getAuthPreferences() {
        return DEFAULT_SCHEME_PRIORITY;
    }

    @Override
    public Map<String, Header> getChallenges(HttpResponse response, HttpContext context) throws MalformedChallengeException {
        Map<String, Header> challengeHeadersBySchemeName = super.getChallenges(response, context);
        if (challengeHeadersBySchemeName.isEmpty()) {
            challengeHeadersBySchemeName.put(JiveCoreConstants.JIVE_CORE_AUTH_SCHEME_NAME, new BasicHeader(AUTH.WWW_AUTH, JiveCoreConstants.JIVE_CORE_AUTH_SCHEME_NAME));
        }

        return challengeHeadersBySchemeName;
    }
}
