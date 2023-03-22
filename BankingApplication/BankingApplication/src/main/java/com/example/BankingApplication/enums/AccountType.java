package com.example.BankingApplication.enums;

public enum AccountType {
    SAVINGS("Savings"),
    CURRENT("Current");
    private String accountType;

    public String getAccountType() {
        return accountType;
    }
    AccountType(String accountType){
        this.accountType=accountType;
    }
}
