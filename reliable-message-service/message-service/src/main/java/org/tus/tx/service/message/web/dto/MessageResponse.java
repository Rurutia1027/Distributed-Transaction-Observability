package org.tus.tx.service.message.web.dto;

import java.time.Instant;

public class MessageResponse {
    private String messageId;
    private MessageStatusDto status;
    private Integer sendTimes;
    private Instant nextRetryAt;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public MessageStatusDto getStatus() { return status; }
    public void setStatus(MessageStatusDto status) { this.status = status; }
    public Integer getSendTimes() { return sendTimes; }
    public void setSendTimes(Integer sendTimes) { this.sendTimes = sendTimes; }
    public Instant getNextRetryAt() { return nextRetryAt; }
    public void setNextRetryAt(Instant nextRetryAt) { this.nextRetryAt = nextRetryAt; }
}
