package org.tus.tx.rms.domain.enums;

public enum MessageStatus {
    PREPARED("PREPARED"),
    COMMITTED("COMMITTED"),
    ROLLED_BACK("ROLLED_BACK"),
    RECORDED("RECORDED"),
    SENDING("SENDING"),
    SENT("SENT"),
    DEAD("DEAD");

    private final String value;

    MessageStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MessageStatus fromValue(String value) {
        for (MessageStatus status : MessageStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
