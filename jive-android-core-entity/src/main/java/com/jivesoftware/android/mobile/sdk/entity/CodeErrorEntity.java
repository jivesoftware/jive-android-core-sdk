package com.jivesoftware.android.mobile.sdk.entity;

public class CodeErrorEntity implements ErrorEntity {
    private Integer code;
    private String message;

    public CodeErrorEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getDescription() {
        return message;
    }

    @Override
    public Integer getErrorCode() {
        return code;
    }

    @Override
    public String getAPIErrorCode() {
        return code == null ? null : Integer.toString(code, 10);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeErrorEntity that = (CodeErrorEntity) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CodeErrorEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}