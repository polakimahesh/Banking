package com.example.BankingApplication.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BankDto {
    @NotBlank(message = "Bank name is required")
    private  String name;
    @NotBlank(message = "Ifsc code is required")
    private String ifscCode;
    @NotBlank(message = "branch is required")
    private String branch;
}
