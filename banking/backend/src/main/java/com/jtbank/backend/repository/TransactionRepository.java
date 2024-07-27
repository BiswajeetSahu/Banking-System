
package com.jtbank.backend.repository;

import com.jtbank.backend.constant.TransactionMode;
import com.jtbank.backend.Model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
     List<Transaction>
     findByModeAndAccountAccountNumber(TransactionMode mode, long accountNumber,
                                       Pageable pageable);

     long
     countByModeAndAccountAccountNumber(TransactionMode mode, long accountNumber);
}
