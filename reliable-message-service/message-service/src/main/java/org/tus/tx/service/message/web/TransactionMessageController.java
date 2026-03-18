package org.tus.tx.service.message.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.tus.common.domain.model.PageResponse;
import org.tus.tx.service.message.web.dto.ConfirmSendRequest;
import org.tus.tx.service.message.web.dto.MessageResponse;
import org.tus.tx.service.message.web.dto.MessageStatusDto;
import org.tus.tx.service.message.web.dto.PageQueryResponse;
import org.tus.tx.service.message.web.dto.PublishMessageRequest;
import org.tus.tx.service.message.entity.TransactionMessage;
import org.tus.tx.service.message.service.TransactionMessageApplicationService;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class TransactionMessageController {

    private final TransactionMessageApplicationService applicationService;

    public TransactionMessageController(TransactionMessageApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public MessageResponse publish(@Valid @RequestBody PublishMessageRequest request) {
        TransactionMessage msg = new TransactionMessage();
        msg.setMessageId(request.getMessageId());
        msg.setConsumerQueue(request.getConsumerQueue());
        msg.setMessageBody(request.getMessageBody());
        msg.setMessageDataType(request.getMessageDataType());
        msg.setField1(request.getField1());
        msg.setField2(request.getField2());
        msg.setField3(request.getField3());

        TransactionMessage saved = applicationService.createWaitingConfirm(msg);
        return toResponse(saved);
    }

    @PostMapping("/{messageId}/confirm")
    public MessageResponse confirm(@PathVariable("messageId") String messageId, @Valid @RequestBody ConfirmSendRequest request) {
        if (!messageId.equals(request.getMessageId())) {
            throw new IllegalArgumentException("path messageId != body messageId");
        }
        TransactionMessage updated = applicationService.markConfirmToSend(messageId);
        return toResponse(updated);
    }

    @GetMapping
    public PageQueryResponse<MessageResponse> list(
            @RequestParam(name = "start", defaultValue = "0") int start,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "asc", defaultValue = "false") boolean asc
    ) {
        PageResponse<TransactionMessage> page = applicationService.listPage(start, pageSize, sortBy, asc);
        PageQueryResponse<MessageResponse> resp = new PageQueryResponse<>();
        resp.setStart(page.getStart());
        resp.setPageSize(page.getPageSize());
        resp.setTotal(page.getTotal());
        List<MessageResponse> list = page.getElements() == null ? List.of() : page.getElements().stream().map(this::toResponse).toList();
        resp.setElements(list);
        return resp;
    }

    private MessageResponse toResponse(TransactionMessage msg) {
        MessageResponse resp = new MessageResponse();
        resp.setMessageId(msg.getMessageId());
        resp.setSendTimes(msg.getSendTimes());
        resp.setNextRetryAt(msg.getNextRetryAt());
        resp.setStatus(mapStatus(msg.getStatus()));
        return resp;
    }

    private MessageStatusDto mapStatus(org.tus.tx.service.message.entity.MessageStatus status) {
        if (status == null) {
            return null;
        }
        return MessageStatusDto.valueOf(status.name());
    }
}

