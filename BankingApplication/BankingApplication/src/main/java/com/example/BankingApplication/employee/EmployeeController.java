package com.example.BankingApplication.employee;

import com.example.BankingApplication.Dto.EmployeeDto;
import com.example.BankingApplication.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private  EmployeeService employeeService;
    @PostMapping("/create-employee")
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDto employeeDto){
        var employeePaySlip = employeeService.createEmployee(employeeDto);
        if(Boolean.TRUE.equals(employeePaySlip.get("isSuccess"))){
            return ResponseEntity.ok(employeePaySlip.get("message"));
        }else
            return ResponseEntity.badRequest().body(employeePaySlip.get("message"));
    }

}
