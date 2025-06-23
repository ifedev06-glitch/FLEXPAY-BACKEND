package com.json.flexpay.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private long accountNumber;
    private String accountName;
    private String code;
    private char symbol;
    private double balance;

}
