package com.example.BankingApplication.transaction;

import com.example.BankingApplication.Dto.*;
import com.example.BankingApplication.account.Account;
import com.example.BankingApplication.account.AccountRepository;
import com.example.BankingApplication.enums.TransactionType;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        if(fromAccountBalance>=transactionRequestDto.getAmount() && fromAccountNo.getIsActive() && toAccountNo.getIsActive() && fromAccountNo!=toAccountNo) {
            Transaction transaction = new Transaction();

            transaction.setFromAccountNo(fromAccountNo.getAccountNo());
            transaction.setTransactionTime(LocalDateTime.now());
            transaction.setAmount(transactionRequestDto.getAmount());
            transaction.setFromTransactionType(TransactionType.DEBIT.getTransactionType());
            transaction.setTransactionDescription(transactionRequestDto.getTransactionDescription());
            fromAccountNo.setBalance(fromAccountBalance - transactionRequestDto.getAmount());
            accountRepository.save(fromAccountNo);


            transaction.setToAccountNo(toAccountNo.getAccountNo());
            transaction.setToTransactionType(TransactionType.CREDIT.getTransactionType());
            transactionRepository.save(transaction);
            toAccountNo.setBalance(toAccountNoBalance + transactionRequestDto.getAmount());
            accountRepository.save(toAccountNo);

            response.put("isSuccess",true);
            response.put("message", transaction);
        }else {
            response1.put("message","incorrect Account details, please check it once,\n Thank you!");
            response.put("isSuccess",false);
            response.put("message",response1);
        }
        return response;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public HashMap<String,Object> getAllTransactionsByAccountNo(TransactionAccountDetails accountDetails){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
        List<Transaction> transactions=  transactionRepository.findByFromAccountNo(accountDetails.getAccountNo());
        if(transactions.isEmpty()){
            response1.put("message","incorrect User Account no "+ accountDetails.getAccountNo());
            response.put("isSuccess",false);
            response.put("message",response1);
        }else {
            List<ListOfTransaction> listOfTransactions = new ArrayList<>();
            for (Transaction transaction : transactions) {
                ListOfTransaction listOfTransaction = new ListOfTransaction();
                listOfTransaction.setFromAccountNo(transaction.getFromAccountNo());
                listOfTransaction.setToAccountNo(transaction.getToAccountNo());
                listOfTransaction.setAmount(transaction.getAmount());
                listOfTransaction.setTransactionTime(transaction.getTransactionTime());
                listOfTransactions.add(listOfTransaction);
            }
            accountDetails.setListOfTransactions(listOfTransactions);
            response.put("isSuccess",true);
            response.put("message",accountDetails);

        }
        return response;
    }
    @Transactional
    public HashMap<String,Object> getAllTransactionByTypeAndDate(TransactionFilterDto transactionFilterDto){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> response1= new HashMap<>();
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        LocalDate localDate=transactionFilterDto.getTransactionTime();
        LocalDateTime startOfDay=localDate.atTime(LocalTime.MIDNIGHT);
        LocalDateTime endDay = localDate.atTime(LocalTime.MAX);
//        String formatDate= outputFormat.format(transactionFilterDto.getTransactionTime());

        List<Transaction> transactions =transactionRepository.findByFromTransactionTypeAndTransactionTimeBetween(transactionFilterDto.
                getFromTransactionType(), startOfDay,endDay);
        if(!transactions.isEmpty()) {

            List<ListOfTransactionFilterDto> listOfTransactionFilterDtos = new ArrayList<>();
            for (Transaction transaction : transactions) {
                ListOfTransactionFilterDto listOfTransactionFilterDto = new ListOfTransactionFilterDto();
                listOfTransactionFilterDto.setFromAccountNo(transaction.getFromAccountNo());
                listOfTransactionFilterDto.setToAccountNo(transaction.getToAccountNo());
                listOfTransactionFilterDto.setAmount(transaction.getAmount());
                listOfTransactionFilterDtos.add(listOfTransactionFilterDto);
            }
            transactionFilterDto.setListOfTransactionFilterDtos(listOfTransactionFilterDtos);
            response.put("message", transactionFilterDto);
            return response;
        }else{
            response1.put("message","Enter correct details ");
            response.put("isSuccess",false);
            response.put("message",response1);
            return response;
        }
    }

    public ByteArrayInputStream createPdfForTransaction(Long fromAccountNo){
       var account = accountRepository.findByAccountNo(fromAccountNo);
       List<Transaction> transactions=transactionRepository.findByFromAccountNo(fromAccountNo);
        String title="Bank Statement";
        String content=account.getUsers().getFirstName()+" "+account.getUsers().getLastName()+" your bank statement!";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document,out);

        //page footer
        HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setBorderWidthBottom(0);
        document.setFooter(footer);


        document.open();
        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD,20, Color.BLACK);
        Paragraph heading = new Paragraph(title,fontTitle);
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingAfter(20);
        document.add(heading);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,15);
        Paragraph paragraph = new Paragraph(content,paraFont);
        paragraph.setSpacingAfter(20);
        document.add(paragraph);

        //users details
        document.add(new Paragraph("Name : "+account.getUsers().getFirstName()));
        document.add(new Paragraph("Account No: "+account.getAccountNo()));
        document.add(new Paragraph("Bank Name: "+account.getBank().getName()));
        document.add(new Paragraph("Total Balance: "+account.getBalance()));
        document.add(new Paragraph("Account Type: "+account.getAccountType()));
        Paragraph spaces = new Paragraph();
        spaces.setSpacingAfter(20);
        document.add(spaces);

        //creating  table columns
        PdfPTable table = new PdfPTable(4);

        table.setWidthPercentage(100);
        table.setWidths(new int[]{10,10,10,10});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell;

        hcell = new PdfPCell(new Phrase("From Account No", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("To Account No", headFont));
        table.addCell(hcell);


        hcell = new PdfPCell(new Phrase("Amount", headFont));
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Transaction Date", headFont));

        table.addCell(hcell);


        for(Transaction transaction :transactions)
        {
            table.addCell(String.valueOf(transaction.getFromAccountNo()));
            table.addCell(String.valueOf(transaction.getToAccountNo()));
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getTransactionTime().getDayOfMonth()+"-"+transaction.getTransactionTime().getMonthValue()+"-"+transaction.getTransactionTime().getYear());

        }
        document.add(table);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

}
