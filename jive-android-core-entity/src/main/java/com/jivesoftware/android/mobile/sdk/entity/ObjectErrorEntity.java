package com.jivesoftware.android.mobile.sdk.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@JsonSerialize(include= NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectErrorEntity implements ErrorEntity {
    @Nonnull
    private final MessageEntity error;

    public ObjectErrorEntity(String message, String code, int status) {
        error = new MessageEntity(message, code, status);
    }

    @JsonCreator
    public ObjectErrorEntity(Map<String,Object> props) {
        HashMap<String, Object> error  = (HashMap<String, Object>) props.get("error");
        this.error = new MessageEntity((String)error.get("message"), (String)error.get("code"),(Integer)error.get("status"));
    }

    @Override
    public String getDescription() {
        return error.message;
    }

    @Override
    public Integer getErrorCode() {
        return error.status;
    }

    @Override
    public String getAPIErrorCode() {
        return error.code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;

        ObjectErrorEntity that = (ObjectErrorEntity) o;

        if (!error.equals(that.error)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return error.hashCode();
    }

    @Override
    public String toString() {
        return "ObjectErrorEntity{" +
                "error=" + error +
                '}';
    }

    @JsonSerialize(include= NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class MessageEntity implements Serializable {
        public String message;
        public String code;
        public int status;

        @JsonCreator
        public MessageEntity(@JsonProperty("message") String message, @JsonProperty("code") String code, @JsonProperty("status") int status) {
            this.message = message;
            this.code = code;
            this.status = status;
        }

        @Override
        public boolean equals(Object o) {
            if ( this == o ) return true;
            if ( o == null || ((Object)this).getClass() != o.getClass() ) return false;

            MessageEntity that = (MessageEntity) o;

            if ( status != that.status ) return false;
            if ( code != null ? !code.equals(that.code) : that.code != null ) return false;
            if ( !message.equals(that.message) ) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = message.hashCode();
            result = 31 * result + (code != null ? code.hashCode() : 0);
            result = 31 * result + status;
            return result;
        }

        @Override
        public String toString() {
            return "MessageEntity{" +
                    "message='" + message + '\'' +
                    ", code='" + code + '\'' +
                    ", status=" + status +
                    '}';
        }
    }
}
