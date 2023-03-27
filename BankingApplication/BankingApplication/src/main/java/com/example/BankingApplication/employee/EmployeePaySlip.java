package com.example.BankingApplication.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmployeePaySlip {
    @Id
    private int id;
    private String name;
    private long accountNo;
    private String accountType;
    private LocalDate payPeriod;
    private  double professionalTax;
    private double grossDeduction;
    private double payAmount;
}
