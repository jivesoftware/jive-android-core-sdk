package com.jivesoftware.android.mobile.sdk.parser;

import com.jivesoftware.android.mobile.sdk.entity.ErrorEntity;
import com.jivesoftware.android.mobile.sdk.entity.SimpleErrorEntity;
import com.jivesoftware.android.mobile.sdk.json.InvalidJsonException;
import com.jivesoftware.android.mobile.sdk.json.JiveJson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JiveCoreExceptionFactory {
    @Nonnull
    private final JiveJson jiveJson;

    public JiveCoreExceptionFactory(@Nonnull JiveJson jiveJson) {
        this.jiveJson = jiveJson;
    }

    @Nonnull
    public JiveCoreException createException(@Nonnull HttpResponse httpResponse, int statusCode, @Nullable HttpEntity httpEntity, @Nonnull byte[] contentBodyBytes) {
        ErrorEntity errorEntity;
        JiveCoreException errorEntityParseException;
        try {
            errorEntity = jiveJson.fromJson(new ByteArrayInputStream(contentBodyBytes), ErrorEntity.class);
            if (errorEntity == null) {
                errorEntityParseException = new JiveCoreUnknownException("Parsed a null ErrorEntity", httpResponse, statusCode, httpEntity, contentBodyBytes);
            } else {
                errorEntityParseException = null;
            }
        } catch (InvalidJsonException e) {
            errorEntity = null;
            errorEntityParseException = new JiveCoreInvalidJsonException(null, e, httpResponse, statusCode);
        } catch (IOException e) {
            errorEntity = null;
            // since we already read the contentBodyBytes, we shouldn't get any IOException
            // but I'm not confident enough in that to throw an AssertionError
            errorEntityParseException = new JiveCoreUnknownException(e, httpResponse, statusCode, httpEntity, contentBodyBytes);
        }

        if (isLoginRequiredError(statusCode, errorEntity)) {
            return new JiveCoreLoginRequiredException(httpResponse, statusCode, errorEntity);
        }

        if (errorEntity == null) {
            return errorEntityParseException;
        }

        Integer errorCode = errorEntity.getErrorCode();
        if (isOAuthError(statusCode, errorEntity)) {
            if (errorCode == null) {
                return new JiveCoreOAuthException(httpResponse, statusCode, errorEntity);
            } else if (JiveCoreOAuthTemporarilyUnavailableException.ERROR_CODE == errorCode) {
                return new JiveCoreOAuthTemporarilyUnavailableException(httpResponse, statusCode, errorEntity);
            } else if (JiveCoreOAuthInvalidClientException.ERROR_CODE == errorCode) {
                return new JiveCoreOAuthInvalidClientException(httpResponse, statusCode, errorEntity);
            } else {
                return new JiveCoreOAuthException(httpResponse, statusCode, errorEntity);
            }
        }

        String apiErrorCode = errorEntity.getAPIErrorCode();
        return new JiveCoreAPIException(httpResponse, statusCode, errorEntity, apiErrorCode);
    }

    public static boolean isLoginRequiredError(int responseCode, ErrorEntity errorEntity) {
        if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
            return true;
        } else if ((responseCode == HttpStatus.SC_BAD_REQUEST || responseCode == HttpStatus.SC_FORBIDDEN)
                && errorEntity instanceof SimpleErrorEntity) {
            Integer errorCode = errorEntity.getErrorCode();
            return errorCode == SimpleErrorEntity.OAuth2ErrorType.INVALID_GRANT.ordinal() ||
                    errorCode == SimpleErrorEntity.OAuth2ErrorType.ACCESS_DENIED.ordinal();
        }

        return false;
    }

    public static boolean isOAuthError(int responseCode, ErrorEntity errorEntity) {
        if (responseCode == HttpStatus.SC_BAD_REQUEST && errorEntity instanceof SimpleErrorEntity) {
            final Integer errorCode = errorEntity.getErrorCode();

            return errorCode != -1 && errorCode != SimpleErrorEntity.OAuth2ErrorType.ACCESS_DENIED.ordinal() &&
                    errorCode != SimpleErrorEntity.OAuth2ErrorType.INVALID_GRANT.ordinal();
        }

        return false;
    }
}
