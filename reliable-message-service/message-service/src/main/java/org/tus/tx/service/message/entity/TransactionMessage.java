package org.tus.tx.service.message.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.tus.common.domain.persistence.PersistedObject;

@Getter
@Setter
@Data
public class TransactionMessage extends PersistedObject {
    private String messageId;
    private String messageBody;
    private String messageDataType;
    private String consumerQueue;
    private Integer messageSendTimes;
    private String alreadyDead;
    private String field1;
    private String field2;
    private String field3;

    public TransactionMessage() {
        super();
    }

    public TransactionMessage(String messageId, String messageBody,
                              String consumerQueue) {
        super();
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.consumerQueue = consumerQueue;
    }
}

