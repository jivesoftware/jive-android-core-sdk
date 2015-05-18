package com.jivesoftware.android.mobile.sdk.core;

import com.jivesoftware.android.httpclient.util.JiveEntityUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

@ParametersAreNonnullByDefault
public class JiveCoreUnauthenticatedRequestFactory {
    @Nonnull
    private final String oauthCredentials;
    @Nonnull
    private final JiveCoreURIFactory uriFactory;
    @Nonnull
    private final String addonUUID;

    public JiveCoreUnauthenticatedRequestFactory(URL baseURL, String oauthCredentials, String addonUUID) {
        this.oauthCredentials = oauthCredentials;
        this.uriFactory = new JiveCoreURIFactory(baseURL);
        this.addonUUID = addonUUID;
    }

    @Nonnull
    public static URL getBaseURLFromFetchVersionLocation(String fetchVersionLocation) throws JiveCoreInvalidLocationException {
        int lastApiVersionIndex = fetchVersionLocation.lastIndexOf("api/version");
        if (lastApiVersionIndex == -1) {
            throw new JiveCoreInvalidLocationException();
        } else {
            String baseLocation = fetchVersionLocation.substring(0, lastApiVersionIndex);
            URL baseURL;
            try {
                baseURL = new URL(baseLocation);
            } catch (MalformedURLException e) {
                throw new JiveCoreInvalidLocationException(e);
            }
            return baseURL;
        }
    }

    @Nonnull
    public HttpGet fetchVersion() {
        URI fetchVersionURI = uriFactory.apiVersionUri();
        HttpGet versionHttpGet = new HttpGet(fetchVersionURI);
        HttpParams versionHttpGetHttpParams = versionHttpGet.getParams();
        HttpClientParams.setRedirecting(versionHttpGetHttpParams, false);

        return versionHttpGet;
    }

    @Nonnull
    public HttpGet fetchPublicMetadataProperties() {
        URI fetchPublicMetadataPropertiesURI = uriFactory.metadataPublicPropertiesUri();
        HttpGet publicMetadataPropertiesHttpGet = new HttpGet(fetchPublicMetadataPropertiesURI);
        HttpParams publicMetdataPropertiesHttpGetHttpParams = publicMetadataPropertiesHttpGet.getParams();
        HttpClientParams.setRedirecting(publicMetdataPropertiesHttpGetHttpParams, false);

        return publicMetadataPropertiesHttpGet;
    }

    @Nonnull
    public HttpPost authorizeDevice(String username, String password) {
        URI uri = uriFactory.oAuth2RequestUri();
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + oauthCredentials);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
        bodyNameValuePairs.add(new BasicNameValuePair("username", username));
        bodyNameValuePairs.add(new BasicNameValuePair("password", password));

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return authorizeDeviceHttpPost;
    }

    @Nonnull
    public HttpPost refreshToken(String refreshToken) {
        URI uri = uriFactory.oAuth2RefreshUri();
        HttpPost authorizeDeviceHttpPost = new HttpPost(uri);
        authorizeDeviceHttpPost.setHeader(JiveCoreHeaders.AUTHORIZATION, "Basic " + oauthCredentials);

        ArrayList<BasicNameValuePair> bodyNameValuePairs = new ArrayList<BasicNameValuePair>();
        bodyNameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
        bodyNameValuePairs.add(new BasicNameValuePair("refresh_token", refreshToken));

        authorizeDeviceHttpPost.setEntity(JiveEntityUtil.createForm(bodyNameValuePairs));

        return authorizeDeviceHttpPost;
    }

    @Nonnull
    public HttpGet fetchSessionGrant() {
        URI uri = uriFactory.sessionGrantUri(addonUUID);
        HttpGet fetchSessionGrantHttpGet = new HttpGet(uri);
        HttpParams fetchSessionGrantHttpGetHttpParams = fetchSessionGrantHttpGet.getParams();
        HttpClientParams.setRedirecting(fetchSessionGrantHttpGetHttpParams, false);

        return fetchSessionGrantHttpGet;
    }
}
