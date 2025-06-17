package com.json.flexpay.service;

import com.json.flexpay.entity.Card;
import com.json.flexpay.entity.User;
import com.json.flexpay.helper.AccountHelper;
import com.json.flexpay.repository.CardRepository;
import com.json.flexpay.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardService {

    private CardRepository cardRepository;
    private TransactionService transactionService;
    private AccountHelper accountHelper;

    public Card getCard(User user) {
        return cardRepository.findByOwnerUid(user.getUid()).orElseThrow();
    }

    public Card createCard(double amount, User user) throws Exception {
        if(amount < 2) {
            throw new IllegalArgumentException("Amount should be at least $2");
        }
        if(!accountHelper.existsByCodeAndOwnerUid("USD", user.getUid())) {
            throw new IllegalArgumentException("USD Account not found for this user so card cannot be created");
        }
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        accountHelper.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        long cardNumber;
        do{
            cardNumber = generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));
        Card card = Card.builder()
                .cardHolder(user.getFirstname() + " " + user.getLastname())
                .cardNumber(cardNumber)
                .exp(LocalDateTime.now().plusYears(3))
                .owner(user)
                .cvv(new RandomUtil().generateRandom(3).toString())
                .balance(amount - 1)
                .build();
        card = cardRepository.save(card);
        transactionService.createAccountTransaction(1, Type.WITHDRAW, 0.00, user, usdAccount);
        transactionService.createAccountTransaction(amount-1, Type.WITHDRAW, 0.00, user, usdAccount);
        transactionService.createCardTransaction(amount-1, Type.WITHDRAW, 0.00, user, card);
        accountHelper.save(usdAccount);
        return card;
    }
}
