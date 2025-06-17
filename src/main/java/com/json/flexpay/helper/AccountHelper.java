package com.json.flexpay.helper;


import com.json.flexpay.dto.AccountDto;
import com.json.flexpay.dto.ConvertDto;
import com.json.flexpay.entity.Account;
import com.json.flexpay.entity.Transaction;
import com.json.flexpay.entity.Type;
import com.json.flexpay.entity.User;
import com.json.flexpay.exceptions.InsufficientFundsException;
import com.json.flexpay.exceptions.UnauthorizedOperationException;
import com.json.flexpay.repository.AccountRepository;
import com.json.flexpay.service.ExchangeRateService;
import com.json.flexpay.service.TransactionService;
import com.json.flexpay.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "NGN", "Nigerian Naira",
            "INR", "Indian Rupee"
    );

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
            validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        do{
            accountNumber = new RandomUtil().generateRandom(10);
        } while(accountRepository.existsByAccountNumber(accountNumber));

        var account = Account.builder()
                .accountNumber(accountNumber)
                .accountName(user.getFirstname() + " " + user.getLastname())
                .balance(1000)
                .owner(user)
                .code(accountDto.getCode())
                .symbol(accountDto.getSymbol())
                .label(CURRENCIES.get(accountDto.getCode()))
                .build();
        return accountRepository.save(account);
    }


    public void validateAccountNonExistsForUser(String code, String uid) throws Exception {
        if (accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new BadRequestException("Account of this type already exist for this user");
        }

    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount, User user) throws Exception {
        validateSufficientFunds(senderAccount, (amount * 1.01));
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        var senderTransaction = transactionService.createAccountTransaction(amount, Type.WITHDRAW, amount * 0.01, user, senderAccount);
        var receiverTransaction = transactionService.createAccountTransaction(amount, Type.DEPOSIT, 0.00, receiverAccount.getOwner(), receiverAccount);

        return senderTransaction;
}

    private void validateSufficientFunds(Account account, double amount) {
        if(account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in the account");
        }
    }

    public boolean existsByCodeAndOwnerUid(String code, String uid) {

        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }

    public Optional<Account> findByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.findByCodeAndOwnerUid(code, uid);
    }
}
