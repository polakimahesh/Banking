package com.example.BankingApplication.transaction;

import com.example.BankingApplication.Dto.TransactionAccountDetails;
import com.example.BankingApplication.Dto.TransactionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("")
    public ResponseEntity<Object> generateTransactions(@RequestBody TransactionRequestDto transactionRequestDto){
        var transaction=transactionService.createTransaction(transactionRequestDto);
        if(Boolean.TRUE.equals(transaction.get("isSuccess"))){
            return ResponseEntity.ok(transaction.get("message"));
        }else
            return ResponseEntity.badRequest().body(transaction.get("message"));
    }
    @GetMapping("getAll")
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }
    @PostMapping("get-transaction-by-accountNo")
    public ResponseEntity<List<Transaction>> getAllTransactionsByAccountNo(@RequestBody TransactionAccountDetails transactionAccountDetails){
        return new ResponseEntity<>(transactionService.getAllTransactionsByAccountNo(transactionAccountDetails),HttpStatus.OK);
    }
}
