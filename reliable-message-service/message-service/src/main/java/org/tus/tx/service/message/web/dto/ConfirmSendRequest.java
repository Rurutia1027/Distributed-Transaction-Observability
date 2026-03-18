package org.tus.tx.service.message.web.dto;

import jakarta.validation.constraints.NotBlank;

public class ConfirmSendRequest {
    @NotBlank
    private String messageId;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
}
