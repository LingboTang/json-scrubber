package com.example.demo;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BusinessData {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // We only consider number with region code 1 (Canada and USA)
    private String phoneNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;

    public BusinessData() {

    }

    public BusinessData(String firstName, String lastName, String email, String password, String phoneNumber, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition position = new ParsePosition(0);
        Date date = (Date)formatter.parse(dateOfBirth, position);
        this.dateOfBirth = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLasttName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }


}
