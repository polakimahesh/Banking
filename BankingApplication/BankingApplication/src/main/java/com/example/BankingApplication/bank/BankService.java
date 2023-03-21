package com.example.BankingApplication.bank;

import com.example.BankingApplication.Dto.BankDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public  Bank createBank(BankDto bankDto){
        Bank bank = new Bank();
        bank.setBranch(bankDto.getBranch());
        bank.setName(bankDto.getName());
        bank.setIfscCode(bankDto.getIfscCode());
        bankRepository.save(bank);
        return  bank;
    }

    public List<Bank> getAllBanks(){
        return bankRepository.findAll();
    }

    public HashMap<String,Object> getSingleBank(int id){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        var bank = bankRepository.findById(id);
        if(bank.isEmpty()){
            response1.put("message","incorrect Bank id "+id+", please enter the valid id!");
            response.put("isSuccess",false);
            response.put("message",response1);
            return  response;
        }
        response.put("isSuccess",true);
        response.put("message",bank);
        return  response;
    }
}
