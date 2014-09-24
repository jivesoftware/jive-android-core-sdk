package com.jivesoftware.android.mobile.sdk.util;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreConstants;

import java.net.MalformedURLException;
import java.net.URL;

public class JiveURLUtil {
    public static boolean isAPIUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String path = url.getPath();
            return path != null && path.contains(JiveCoreConstants.CORE_API_V3_PREFIX);
        } catch (MalformedURLException e) {
            // May be a relative path, just check index
            return urlString != null && urlString.contains(JiveCoreConstants.CORE_API_V3_PREFIX);
        }
        catch (Exception e) {
            return false;
        }
    }

    public static String extractAPIEndpoint(String url) {
        String apiUrl =  url;
        if ( isAPIUrl(url) ) {
            int apiPortionStart = url.indexOf(JiveCoreConstants.CORE_API_V3_PREFIX);
            if ( apiPortionStart >= 0 ) {
                int apiEndpointStart = apiPortionStart + JiveCoreConstants.CORE_API_V3_PREFIX.length();
                int apiEndpointEnd = url.indexOf('?', apiEndpointStart);
                apiUrl = apiEndpointEnd > 0 ? url.substring(apiEndpointStart, apiEndpointEnd) : url.substring(apiEndpointStart);
            }
        }

        return apiUrl;
    }

    // only for tests.
    public static URL createURL(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
