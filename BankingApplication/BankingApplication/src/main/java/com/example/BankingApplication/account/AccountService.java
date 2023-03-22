package com.example.BankingApplication.account;

import com.example.BankingApplication.Dto.AccountDto;
import com.example.BankingApplication.Dto.AccountResponseDto;
import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.bank.BankRepository;
import com.example.BankingApplication.enums.AccountType;
import com.example.BankingApplication.users.Users;
import com.example.BankingApplication.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private UsersRepository usersRepository;

    private Long getAccountNumber(){
        Random random = new Random();
        String accountNo="";
        for(int i=0;i<12;i++){
            int randomNumber=random.nextInt(10);
            accountNo+=randomNumber;
        }
        return  Long.parseLong(accountNo);
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }


    public Account getAccountByAccountNo(AccountResponseDto accountResponseDto){
//        Account account=accountRepository.findByAccountNo(accountResponseDto.getAccountNo());
//        Bank bank=bankRepository.findById(accountResponseDto.getBankId()).orElse(null);
//        Users users = usersRepository.findById(accountResponseDto.getUserId()).orElse(null);

        return null;
    }



    public Account createAccount(AccountDto accountDto){
        Account account = new Account();
        Bank bank = bankRepository.findById(accountDto.getBankId()).orElse(null);
        Users users =usersRepository.findById(accountDto.getUserId()).orElse(null);

        account.setAccountNo(getAccountNumber());
        account.setBank(bank);
        account.setUsers(users);
        if(accountDto.getAccountType()!=null && "Current".equalsIgnoreCase(accountDto.getAccountType())){
            account.setAccountType(AccountType.CURRENT.getAccountType());
        }else {
            account.setAccountType(AccountType.SAVINGS.getAccountType());
        }
        account.setBalance(accountDto.getBalance());
        accountRepository.save(account);
       return account;
    }


}
