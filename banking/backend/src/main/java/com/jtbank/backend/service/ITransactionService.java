
package com.jtbank.backend.service;

import com.jtbank.backend.constant.TransactionMode;
import com.jtbank.backend.Model.Transaction;

import java.util.List;

public interface ITransactionService {
    void addTransaction(Transaction transaction, long accountNumber);

    List<Transaction> getDebitedTransactions(long accountNumber);

    List<Transaction> getCreditedTransactions(long accountNumber, int pageSize, int pageNumber);

    List<Transaction> getTransferredTransactions(long accountNumber);

    long countRecord(TransactionMode mode, long accountNumber);

    void addTransaction(Transaction transaction);

    List<Transaction> getDebitedTransaction(long accountNumber, int pageSize, int pageNumber);
}
