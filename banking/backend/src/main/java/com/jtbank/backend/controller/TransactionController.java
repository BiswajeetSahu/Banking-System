
package com.jtbank.backend.controller;

import com.jtbank.backend.constant.TransactionMode;
import com.jtbank.backend.dto.DatatableDTO;
import com.jtbank.backend.dto.TransactionDTO;
import com.jtbank.backend.mapper.TransactionMapper;
import com.jtbank.backend.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService service;

    @GetMapping("/credit")
    public DatatableDTO creditedTransactions(@RequestHeader long accountNumber,
                                             @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                             @RequestParam(required = false, defaultValue = "10") int pageSize) {
        var results = service.getCreditedTransactions(accountNumber, pageNumber, pageSize);
        var transactions = results.stream().map(TransactionMapper::dtoMapper).toList();

        var totalRecord = service.countRecord(TransactionMode.CREDIT, accountNumber);

        return new DatatableDTO(totalRecord, pageNumber, pageSize, transactions);
    }

    @GetMapping("/{accountNumber}/debit")
    public List<TransactionDTO> debitedTransactions(@RequestHeader long accountNumber) {
        var results = service.getDebitedTransactions(accountNumber);
        return results.stream().map(TransactionMapper::dtoMapper).toList();
    }

    @GetMapping("/{accountNumber}/transfer")
    public List<TransactionDTO> transferedTransactions(@RequestHeader long accountNumber) {
        var results = service.getTransferredTransactions(accountNumber);
        return results.stream().map(TransactionMapper::dtoMapper).toList();
    }
}
