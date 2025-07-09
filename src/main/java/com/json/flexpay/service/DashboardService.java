package com.json.flexpay.service;

import com.json.flexpay.dto.DashboardResponse;
import com.json.flexpay.dto.TransactionsDto;
import com.json.flexpay.entity.Account;
import com.json.flexpay.entity.Transaction;
import com.json.flexpay.entity.User;
import com.json.flexpay.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {


    public DashboardResponse getDashboardInfo(User user) {
        Account account = user.getAccounts().stream()
                .findFirst()
                .orElseThrow(() -> new BadRequestException("User has no account"));
                double balance = account.getBalance();
        List<Transaction> transactions = account.getTransactions();
        List<TransactionsDto> transactionDtos = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .map(tx -> TransactionsDto.builder()
                        .txId(tx.getTxId())
                        .type(tx.getType().toString())
                        .amount(tx.getAmount())
                        .description(tx.getDescription())
                        .createdAt(tx.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return DashboardResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .balance(balance)
                .accountNumber(account.getAccountNumber())
                .transactions(transactionDtos)
                .build();

    }
}
