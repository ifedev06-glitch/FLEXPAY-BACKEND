package com.json.flexpay.dto;

import com.json.flexpay.entity.Status;
import com.json.flexpay.entity.Type;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class TransactionResponse {

    private String txId;
    private Double amount;
    private Double txFee;

    private String senderName;
    private Long senderAccountNumber;

    private String receiverName;
    private Long receiverAccountNumber;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
    private Type type;
}
