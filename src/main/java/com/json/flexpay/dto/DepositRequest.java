package com.json.flexpay.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DepositRequest {

    private long recipientAccountNumber;
    private double amount;
    private String description;

}
