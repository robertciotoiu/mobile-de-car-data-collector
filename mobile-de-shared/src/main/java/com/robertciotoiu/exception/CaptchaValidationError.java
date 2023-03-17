package com.robertciotoiu.exception;

public class CaptchaValidationError extends Error{
    public CaptchaValidationError(String message) {
        super(message);
    }
}
