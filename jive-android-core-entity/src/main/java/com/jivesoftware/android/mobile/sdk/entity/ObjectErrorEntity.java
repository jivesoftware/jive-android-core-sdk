package com.jivesoftware.android.mobile.sdk.entity;

import javax.annotation.Nonnull;
import java.io.Serializable;

public class ObjectErrorEntity implements ErrorEntity {
    @Nonnull
    private final MessageEntity error;

    public ObjectErrorEntity(String message, int status) {
        error = new MessageEntity(message, status);
    }

    public ObjectErrorEntity(String message, String code, int status) {
        error = new MessageEntity(message, code, status);
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

    private class MessageEntity implements Serializable {
        private String message;
        private String code;
        private int status;

        private MessageEntity(String message, int status) {
            this.message = message;
            this.status = status;
        }

        private MessageEntity(String message, String code, int status) {
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
