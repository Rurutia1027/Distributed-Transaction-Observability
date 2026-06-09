
package com.eda.tracing.exception;

public class TransactionConflictException extends RuntimeException {
    public TransactionConflictException(String message) {
        super(message);
    }
}
