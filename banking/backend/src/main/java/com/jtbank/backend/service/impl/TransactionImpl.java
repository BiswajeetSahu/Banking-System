
package com.jtbank.backend.service.impl;

import com.jtbank.backend.constant.TransactionMode;
import com.jtbank.backend.Model.Account;
import com.jtbank.backend.Model.Transaction;
import com.jtbank.backend.repository.AccountRepository;
import com.jtbank.backend.repository.TransactionRepository;
import com.jtbank.backend.service.IAccountService;
import com.jtbank.backend.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionImpl implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Async
    @Transactional
    @Override
    public void addTransaction(Transaction transaction, long accountNumber) {
        var account = getAccount(accountNumber);
        transaction.setAccount(account);

        var transactions = account.getTransactions();
        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        transaction.setTimestamp(LocalDateTime.now());
        transactions.add(transaction);

        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getDebitedTransactions(long accountNumber) {
        var sort = Sort.by("timestamp").descending();
        var page = PageRequest.of(0, 2, sort);

        return transactionRepository
                .findByModeAndAccountAccountNumber(TransactionMode.DEBIT,
                        accountNumber,  page);
    }

    @Override
    public List<Transaction> getCreditedTransactions(long accountNumber, int pageNumber, int pageSize) {
        var sort = Sort.by("timestamp").descending();
        var page = PageRequest.of(pageNumber - 1, pageSize, sort);

        return transactionRepository
                .findByModeAndAccountAccountNumber(TransactionMode.CREDIT,
                        accountNumber,  page);
    }

    @Override
    public List<Transaction> getTransferredTransactions(long accountNumber) {
        var sort = Sort.by("timestamp").descending();
        var page = PageRequest.of(0, 2, sort);

        return transactionRepository
                .findByModeAndAccountAccountNumber(TransactionMode.TRANSFER,
                        accountNumber,  page);
    }

    @Override
    public long countRecord(TransactionMode mode, long accountNumber) {
        return transactionRepository.countByModeAndAccountAccountNumber(mode, accountNumber);
    }

    @Override
    public void addTransaction(Transaction transaction) {

    }

    @Override
    public List<Transaction> getDebitedTransaction(long accountNumber, int pageSize, int pageNumber) {
        return null;
    }

    private Account getAccount(long accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow();
    }
}
