package com.example.BankingApplication.employee;

import com.example.BankingApplication.Dto.EmployeeDto;
import com.example.BankingApplication.account.Account;
import com.example.BankingApplication.account.AccountRepository;
import com.example.BankingApplication.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;

    public HashMap<String,Object> createEmployee(EmployeeDto employeeDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        Account account = accountRepository.findByAccountNoAndAccountType(employeeDto.getAccountNo(), AccountType.SALARY.getAccountType());
        if(account==null){
            response1.put("message","incorrect account details please enter the valid details, \n Thank you!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            EmployeePaySlip employee = new EmployeePaySlip();
            employee.setName(account.getUsers().getFirstName() + " " + account.getUsers().getLastName());
            employee.setAccountNo(account.getAccountNo());
            employee.setAccountType(account.getAccountType());
            employee.setPayPeriod(LocalDate.now());
            employee.setPayAmount(employeeDto.getPayAmount());
            employee.setProfessionalTax(employeeDto.getProfessionalTax());
            employee.setGrossDeduction(employee.getProfessionalTax());
            account.setBalance(employee.getPayAmount() - employee.getProfessionalTax());
            accountRepository.save(account);
            employeeRepository.save(employee);
            response1.put("message","Employee Created Successfully!");
            response.put("isSuccess",true);
            response.put("message",response1);
        }
        return response;
    }
}
