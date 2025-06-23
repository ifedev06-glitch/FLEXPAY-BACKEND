package com.json.flexpay.controller;

import com.json.flexpay.dto.DepositRequest;
import com.json.flexpay.dto.TransactionResponse;
import com.json.flexpay.dto.TransferDto;
import com.json.flexpay.dto.WithdrawRequest;
import com.json.flexpay.entity.Account;
import com.json.flexpay.entity.Transaction;
import com.json.flexpay.entity.User;
import com.json.flexpay.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/get")
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @RequestBody DepositRequest request,
            @AuthenticationPrincipal User user
    ) throws Exception {
        var tx = accountService.depositFunds(request, user);
        return ResponseEntity.ok(tx);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @RequestBody WithdrawRequest request,
            @AuthenticationPrincipal User user
    ) throws Exception {
        Transaction tx = accountService.withdrawFunds(request, user);
        return ResponseEntity.ok(tx);
    }

}
