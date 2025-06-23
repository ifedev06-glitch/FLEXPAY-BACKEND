package com.json.flexpay.service;


import com.json.flexpay.dto.DepositRequest;
import com.json.flexpay.dto.TransferDto;
import com.json.flexpay.dto.WithdrawRequest;
import com.json.flexpay.entity.Account;
import com.json.flexpay.entity.Transaction;
import com.json.flexpay.entity.Type;
import com.json.flexpay.entity.User;
import com.json.flexpay.helper.AccountHelper;
import com.json.flexpay.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final TransactionService transactionService;
    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        System.out.println("Currency Code: " + transferDto.getCode());
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
                .orElseThrow(() -> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto.getAmount(), user);
    }

    public Transaction depositFunds(DepositRequest request, User user) throws Exception {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new Exception("Account not found"));

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);

        return transactionService.createAccountTransaction(
                request.getAmount(),
                Type.DEPOSIT,
                0.0,
                user,
                account,
                null,
                null,
                request.getDescription()
        );
    }

    public Transaction withdrawFunds(WithdrawRequest request, User user) throws Exception {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new Exception("Account not found"));

        double currentBalance = account.getBalance();
        double withdrawalAmount = request.getAmount();

        if (withdrawalAmount > currentBalance) {
            throw new Exception("Insufficient balance");
        }

        account.setBalance(currentBalance - withdrawalAmount);
        accountRepository.save(account);

        return transactionService.createAccountTransaction(
                withdrawalAmount,
                Type.WITHDRAW,
                0.0,
                user,
                account,
                null,
                null,
                request.getDescription()
        );
    }

}
