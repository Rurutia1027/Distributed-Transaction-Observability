package org.tus.tx.service.message.web.dto;

import jakarta.validation.constraints.NotBlank;

public class PublishMessageRequest {

    private String messageId;

    @NotBlank
    private String consumerQueue;

    @NotBlank
    private String messageBody;

    private String messageDataType;

    private String field1;
    private String field2;
    private String field3;

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getConsumerQueue() { return consumerQueue; }
    public void setConsumerQueue(String consumerQueue) { this.consumerQueue = consumerQueue; }
    public String getMessageBody() { return messageBody; }
    public void setMessageBody(String messageBody) { this.messageBody = messageBody; }
    public String getMessageDataType() { return messageDataType; }
    public void setMessageDataType(String messageDataType) { this.messageDataType = messageDataType; }
    public String getField1() { return field1; }
    public void setField1(String field1) { this.field1 = field1; }
    public String getField2() { return field2; }
    public void setField2(String field2) { this.field2 = field2; }
    public String getField3() { return field3; }
    public void setField3(String field3) { this.field3 = field3; }
}
