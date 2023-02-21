package com.robertciotoiu.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaginationNotFoundErrorTest {
    @Test
    void testConstructor() {
        String message = "Test message";

        PaginationNotFoundError ex = new PaginationNotFoundError(message);

        assertEquals(message, ex.getMessage());
    }

    @Test
    void testThrow() {
        String message = "Test message";

        try {
            throw new PaginationNotFoundError(message);
        } catch (PaginationNotFoundError ex) {
            assertEquals(message, ex.getMessage());
        }
    }
}