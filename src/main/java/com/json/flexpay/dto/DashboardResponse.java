package com.json.flexpay.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private String firstname;
    private String lastname;
    private double balance;
    private long accountNumber;
    private List<TransactionsDto> transactions;
}
