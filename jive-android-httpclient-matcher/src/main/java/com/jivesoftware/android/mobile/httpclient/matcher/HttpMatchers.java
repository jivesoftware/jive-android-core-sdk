package com.jivesoftware.android.mobile.httpclient.matcher;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import com.jivesoftware.android.mobile.matcher.DowncastMatcher;
import com.jivesoftware.android.mobile.matcher.PropertyMatcher;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;

public class HttpMatchers {
    @Nonnull
    public static <R extends HttpUriRequest> Matcher<R> requestUrl(@Nullable String url) {
        return new PropertyMatcher<String, R>("url", url) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull R item) throws MalformedURLException {
                URI uri = item.getURI();
                if (uri == null) {
                    return null;
                } else {
                    return uri.toString();
                }
            }
        };
    }

    @Nonnull
    public static <R extends HttpUriRequest> Matcher<R> requestMethod(@Nonnull String method) {
        return new PropertyMatcher<String, R>("method", method) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull R item) {
                return item.getMethod();
            }
        };
    }

    @Nonnull
    public static Matcher<Header> headerName(@Nonnull String name) {
        return new PropertyMatcher<String, Header>("name", name) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull Header item) throws Exception {
                return item.getName();
            }
        };
    }

    @Nonnull
    public static Matcher<Header> headerValue(@Nullable String value) {
        return new PropertyMatcher<String, Header>("value", value) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull Header item) throws Exception {
                return item.getValue();
            }
        };
    }

    @Nonnull
    public static Matcher<Header> header(@Nonnull String name, @Nullable String value) {
        return allOf(headerName(name), headerValue(value));
    }

    @Nonnull
    public static <R extends HttpUriRequest> Matcher<R> httpHeadersContains(String name) {
        return httpHeadersContains(name, Matchers.not(Matchers.isEmptyOrNullString()));
    }

    @Nonnull
    public static <R extends HttpUriRequest> Matcher<R> httpHeadersContains(String name, String value) {
        return httpHeadersContains(name, CoreMatchers.equalTo(value));
    }

    @Nonnull
    public static <R extends HttpUriRequest> Matcher<R> httpHeadersContains(final String name, final Matcher<String> valueMatcher) {
        return new TypeSafeMatcher<R>() {
            @Override
            protected boolean matchesSafely(R item) {
                Header[] headers = item.getHeaders(name);
                boolean matches = false;
                for (Header header : headers) {
                    String headerValue = header.getValue();
                    if (valueMatcher.matches(headerValue)) {
                        matches = true;
                        break;
                    }
                }
                return matches;
            }

            @Override
            public void describeTo(Description description) {
                description.
                        appendText("one http headers should match ").
                        appendValue(name).
                        appendText("=");
                valueMatcher.describeTo(description);
            }

            @Override
            protected void describeMismatchSafely(R item, Description mismatchDescription) {
                mismatchDescription.
                        appendText("http headers were:");
                for (Header header : item.getAllHeaders()) {
                    mismatchDescription.
                            appendText("\n\t").
                            appendValue(header.getName()).
                            appendText("=");
                    valueMatcher.describeMismatch(header.getValue(), mismatchDescription);
                }
            }
        };
    }

    private static abstract class BoundaryNormalizingHttpEntityPropertyMatcher extends PropertyMatcher<String, HttpEntity> {
        private BoundaryNormalizingHttpEntityPropertyMatcher(@Nonnull String propertyName, @Nullable String expectedPropertyValue, @Nullable String boundary) {
            super(propertyName, unboundary(expectedPropertyValue, boundary));
        }

        @Nullable
        @Override
        protected final String getPropertyValue(@Nonnull HttpEntity item) throws Exception {
            String itemBoundary = JiveEntityUtil.getMultipartFormBoundary(item);
            String maybeBoundaried = getMaybeBoundariedPropertyValue(item);
            String unboundaried = unboundary(maybeBoundaried, itemBoundary);
            return unboundaried;
        }

        @Nullable
        protected abstract String getMaybeBoundariedPropertyValue(@Nonnull HttpEntity item) throws Exception;

        private static String unboundary(String maybeBoundaried, String boundary) {
            String unboundaried;
            if ((maybeBoundaried == null) || (boundary == null)) {
                unboundaried = maybeBoundaried;
            } else {
                unboundaried = maybeBoundaried.replace(boundary, "<boundary>");
            }
            return unboundaried;
        }
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntityContentType(String contentType, String boundary) {
        return new BoundaryNormalizingHttpEntityPropertyMatcher("contentType", contentType, boundary) {
            @Nullable
            @Override
            protected String getMaybeBoundariedPropertyValue(@Nonnull HttpEntity item) throws Exception {
                String contentType = JiveEntityUtil.getContentType(item);
                return contentType;
            }
        };
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntityContentEncoding(String contentEncoding) {
        return new PropertyMatcher<String, HttpEntity>("contentEncoding", contentEncoding) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull HttpEntity item) throws Exception {
                return JiveEntityUtil.getContentEncoding(item);
            }
        };
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntityContent(String content, String boundary) {
        return new BoundaryNormalizingHttpEntityPropertyMatcher("content", content, boundary) {
            @Nullable
            @Override
            protected String getMaybeBoundariedPropertyValue(@Nonnull HttpEntity item) throws Exception {
                String content = JiveEntityUtil.toString(item);
                return content;
            }
        };
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntity(@Nonnull String name, @Nonnull String value) throws IOException {
        return httpEntity(Arrays.asList(new BasicNameValuePair(name, value)));
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntity(@Nonnull String name1, @Nonnull String value1, @Nonnull String name2, @Nonnull String value2) throws IOException {
        return httpEntity(Arrays.asList(new BasicNameValuePair(name1, value1), new BasicNameValuePair(name2, value2)));
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntity(List<? extends NameValuePair> nameValuePairs) throws IOException {
        return httpEntity(JiveEntityUtil.createForm(nameValuePairs));
    }

    @Nonnull
    public static Matcher<HttpEntity> httpEntity(@Nonnull HttpEntity httpEntity) throws IOException {
        String contentType = JiveEntityUtil.getContentType(httpEntity);
        String contentEncoding = JiveEntityUtil.getContentEncoding(httpEntity);
        String content = JiveEntityUtil.toString(httpEntity);
        String boundary = JiveEntityUtil.getMultipartFormBoundary(httpEntity);
        return allOf(
                httpEntityContentType(contentType, boundary),
                httpEntityContentEncoding(contentEncoding),
                httpEntityContent(content, boundary));
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestEntity(@Nonnull String name, @Nonnull String value) throws IOException {
        return requestEntity(Arrays.asList(new BasicNameValuePair(name, value)));
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestEntity(@Nonnull String name1, @Nonnull String value1, @Nonnull String name2, @Nonnull String value2) throws IOException {
        return requestEntity(Arrays.asList(
                new BasicNameValuePair(name1, value1),
                new BasicNameValuePair(name2, value2)));
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestEntity(@Nonnull String name1, @Nonnull String value1, @Nonnull String name2, @Nonnull String value2, @Nonnull String name3, @Nonnull String value3) throws IOException {
        return requestEntity(Arrays.asList(
                new BasicNameValuePair(name1, value1),
                new BasicNameValuePair(name2, value2),
                new BasicNameValuePair(name3, value3)));
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestEntity(@Nonnull String name1, @Nonnull String value1, @Nonnull String name2, @Nonnull String value2, @Nonnull String name3, @Nonnull String value3, @Nonnull String name4, @Nonnull String value4) throws IOException {
        return requestEntity(Arrays.asList(
                new BasicNameValuePair(name1, value1),
                new BasicNameValuePair(name2, value2),
                new BasicNameValuePair(name3, value3),
                new BasicNameValuePair(name4, value4)));
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestEntity(@Nonnull List<? extends NameValuePair> entityNameValuePairs) throws IOException {
        return requestEntity(JiveEntityUtil.createForm(entityNameValuePairs));
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestEntity(@Nonnull HttpEntity httpEntity) throws IOException {
        return new DowncastMatcher<HttpUriRequest, HttpEntityEnclosingRequestBase>(
                HttpEntityEnclosingRequestBase.class,
                new PropertyMatcher<HttpEntity, HttpEntityEnclosingRequestBase>(
                        httpEntity(httpEntity),
                        "entity") {
                    @Nullable
                    @Override
                    protected HttpEntity getPropertyValue(@Nonnull HttpEntityEnclosingRequestBase item) throws Exception {
                        return item.getEntity();
                    }
                });
    }

    @Nonnull
    public static Matcher<HttpUriRequest> requestHeaders(@Nonnull Matcher<? super Iterable<Header>> headersMatcher) {
        return new PropertyMatcher<List<Header>, HttpUriRequest>(headersMatcher, "headers") {
            @Nullable
            @Override
            protected List<Header> getPropertyValue(@Nonnull HttpUriRequest item) throws Exception {
                Header[] headers = item.getAllHeaders();
                if (headers == null) {
                    return null;
                } else {
                    return Arrays.asList(headers);
                }
            }
        };
    }

    @Nonnull
    public static Matcher<FileBody> fileBodyFilename(@Nonnull String filename) {
        return new PropertyMatcher<String, FileBody>("filename", filename) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull FileBody item) throws Exception {
                return item.getFilename();
            }
        };
    }

    @Nonnull
    public static Matcher<FileBody> fileBodyFile(@Nonnull File file) {
        return new PropertyMatcher<File, FileBody>("file", file) {
            @Nullable
            @Override
            protected File getPropertyValue(@Nonnull FileBody item) throws Exception {
                return item.getFile();
            }
        };
    }

    @Nonnull
    public static Matcher<FileBody> fileBodyMimeType(@Nonnull String mimeType) {
        return new PropertyMatcher<String, FileBody>("mimeType", mimeType) {
            @Nullable
            @Override
            protected String getPropertyValue(@Nonnull FileBody item) throws Exception {
                return item.getMimeType();
            }
        };
    }
}
