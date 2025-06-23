package com.json.flexpay.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class WithdrawRequest {

    private long accountNumber;

    private Double amount;

    private String description;

}
