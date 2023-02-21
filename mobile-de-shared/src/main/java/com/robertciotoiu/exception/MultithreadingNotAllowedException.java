package com.robertciotoiu.exception;

public class MultithreadingNotAllowedException extends RuntimeException {
    public MultithreadingNotAllowedException(String message) {
        super(message);
    }
}
