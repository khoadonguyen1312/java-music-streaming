package com.khoadonguyen.java_music_streaming.error;

import androidx.annotation.NonNull;

public class DynamicError extends Exception {
    private ErrorType errorType;

    public DynamicError(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
