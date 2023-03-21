package com.example.BankingApplication.account;

import com.example.BankingApplication.bank.Bank;
import com.example.BankingApplication.users.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;
    private long accountNO;
    @ManyToOne
    private Users users;
    @OneToOne
    @JoinColumn(unique = true)
    private Bank bank;
    private double balance=0.0;
    private String accountType;

}
