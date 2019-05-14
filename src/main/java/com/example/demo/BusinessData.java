package com.example.demo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BusinessData {

    private String firstName;
    private String lastName;
    private String email;
    private String passWord;

    // We only consider number with region code 1 (Canada and USA)
    private String phoneNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;

    public BusinessData() {

    }

    public BusinessData(String firstName, String lastName, String email, String passWord, String phoneNumber, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }


}
