package com.example.BankingApplication.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsersDto {
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String email;
    private LocalDate dateOfBirth;
    private String password;
}
