package com.jivesoftware.android.mobile.sdk.util;

import com.jivesoftware.android.mobile.sdk.core.JiveCoreQueryParameterProvider;
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
            if (!baseURL.getPath().equals("") && !baseURL.getPath().endsWith("/")) {
                baseURL = new URL(baseURL.toExternalForm() + "/");
            }
            if (pathAndQuery.length() > 1 && pathAndQuery.startsWith("/")) {
                pathAndQuery = pathAndQuery.substring(1);
            }
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
    public static URI createURI(@Nonnull URL baseURL, @Nonnull String pathAndQuery, @Nonnull JiveCoreQueryParameterProvider options) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, List<String>> entry : options.provideQueryParameters().entrySet()) {
            for ( String value : entry.getValue() ) {
                pairs.add(new BasicNameValuePair(entry.getKey(), value));
            }
        }

        String additionalQueryParams = URLEncodedUtils.format(pairs, "UTF-8");
        String additionalQuery;
        if ((additionalQueryParams == null) || (additionalQueryParams.length() == 0)) {
            additionalQuery = "";
        } else if (pathAndQuery.contains("?")) {
            additionalQuery = "&" + additionalQueryParams;
        } else {
            additionalQuery = "?" + additionalQueryParams;
        }
        URI uri = createURI(baseURL, pathAndQuery + additionalQuery);
        return uri;
    }
}
