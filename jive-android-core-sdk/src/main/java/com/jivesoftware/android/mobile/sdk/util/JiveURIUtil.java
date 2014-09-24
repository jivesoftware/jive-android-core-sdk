package com.jivesoftware.android.mobile.sdk.util;

import com.jivesoftware.android.mobile.sdk.core.options.JiveCoreReturnFieldsRequestOptions;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JiveURIUtil {
    @Nonnull
    public static URI createURI(@Nonnull URL baseURL, @Nonnull String pathAndQuery) {
        try {
            URL url = new URL(baseURL, pathAndQuery);
            try {
                URI uri = url.toURI();
                return uri;
            } catch (URISyntaxException e) {
                throw new IllegalStateException("invalid uri: " + url.toExternalForm(), e);
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException("invalid pathAndQuery \"" + pathAndQuery + "\" for baseURL: " + baseURL);
        }
    }

    @Nonnull
    public static URI createURI(@Nonnull URL baseURL, @Nonnull String path, @Nonnull JiveCoreReturnFieldsRequestOptions options) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, List<String>> entry : options.copyQueryParameters().entrySet()) {
            for ( String value : entry.getValue() ) {
                pairs.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        String queryParams = URLEncodedUtils.format(pairs, "UTF-8");
        String query;
        if ((queryParams == null) || (queryParams.length() == 0)) {
            query = "";
        } else {
            query = "?" + queryParams;
        }
        URI uri = createURI(baseURL, path + query);
        return uri;
    }
}
