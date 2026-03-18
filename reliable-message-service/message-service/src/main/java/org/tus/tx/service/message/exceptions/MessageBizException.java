package org.tus.tx.service.message.exceptions;

public class MessageBizException extends RuntimeException {

    public static final int SAVA_MESSAGE_IS_NULL = 8001;
    public static final int MESSAGE_CONSUMER_QUEUE_IS_NULL = 8002;

    private final int errorCode;

    public MessageBizException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public MessageBizException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static MessageBizException saveMessageIsNull() {
        return new MessageBizException(SAVA_MESSAGE_IS_NULL, "Transaction message must not be null");
    }

    public static MessageBizException consumerQueueIsNull() {
        return new MessageBizException(MESSAGE_CONSUMER_QUEUE_IS_NULL, "Consumer queue must not be null or empty");
    }
}

