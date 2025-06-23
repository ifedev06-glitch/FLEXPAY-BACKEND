package com.json.flexpay.controller;


import com.json.flexpay.entity.Transaction;
import com.json.flexpay.entity.User;
import com.json.flexpay.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    @GetMapping
    public List<Transaction> getAllTransactions(@RequestParam(defaultValue = "0")  String page, Authentication auth) {
        return transactionService.getAllTransactions(page, (User) auth.getPrincipal());
    }


}