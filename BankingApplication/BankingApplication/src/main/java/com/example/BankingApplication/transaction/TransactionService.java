package com.example.BankingApplication.transaction;

import com.example.BankingApplication.Dto.TransactionAccountDetails;
import com.example.BankingApplication.Dto.TransactionRequestDto;
import com.example.BankingApplication.account.Account;
import com.example.BankingApplication.account.AccountRepository;
import com.example.BankingApplication.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Transactional
    public HashMap<String,Object> createTransaction(TransactionRequestDto transactionRequestDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();

        Account fromAccountNo= accountRepository.findByAccountNo(transactionRequestDto.getFromAccountNo());
        Account toAccountNo= accountRepository.findByAccountNo(transactionRequestDto.getToAccountNo());

        double fromAccountBalance= fromAccountNo.getBalance();
        double toAccountNoBalance=toAccountNo.getBalance();
        if(fromAccountBalance>=transactionRequestDto.getAmount()) {
            Transaction debitTransaction = new Transaction();

            debitTransaction.setAccountNumber(fromAccountNo.getAccountNo());
            debitTransaction.setTransactionTime(LocalDateTime.now());
            debitTransaction.setAmount(transactionRequestDto.getAmount());
            debitTransaction.setTransactionType(TransactionType.DEBIT.getTransactionType());
            debitTransaction.setTransactionDescription(transactionRequestDto.getTransactionDescription());
            transactionRepository.save(debitTransaction);
            fromAccountNo.setBalance(fromAccountBalance - transactionRequestDto.getAmount());
            accountRepository.save(fromAccountNo);

            Transaction creditTransaction = new Transaction();

            creditTransaction.setAccountNumber(toAccountNo.getAccountNo());
            creditTransaction.setTransactionDescription(transactionRequestDto.getTransactionDescription());
            creditTransaction.setTransactionTime(LocalDateTime.now());
            creditTransaction.setTransactionType(TransactionType.CREDIT.getTransactionType());
            creditTransaction.setAmount(transactionRequestDto.getAmount());
            transactionRepository.save(creditTransaction);
            toAccountNo.setBalance(toAccountNoBalance + transactionRequestDto.getAmount());
            accountRepository.save(toAccountNo);

            response.put("isSuccess",true);
            response.put("message", "Transaction Success! \n Thank You!");
        }else {
            response1.put("message","incorrect Account details, please check once,\n Thank you!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }
        return response;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsByAccountNo(TransactionAccountDetails accountDetails){
        List<Transaction> transaction = transactionRepository.findByAccountNumber(accountDetails.getAccountNo());
        return transaction;
    }


}
