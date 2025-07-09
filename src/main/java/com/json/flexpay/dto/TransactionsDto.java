package com.json.flexpay.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsDto {

    private String txId;
    private String type;
    private double amount;
    private String description;
    private LocalDateTime createdAt;
}
