package com.json.flexpay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {

    private long recipientAccountNumber;

    private double amount;

    private String description;
}