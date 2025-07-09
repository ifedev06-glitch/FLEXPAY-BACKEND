package com.json.flexpay.repository;


import com.json.flexpay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    List<Account> findAllByOwnerUid(String uid);

    Optional<Account> findByOwnerUid( String uid);


    Optional<Account> findByAccountNumber(long recipientAccountNumber);

    Optional<Account> findByCodeAndAccountNumber(String code, long recipientAccountNumber);
}
