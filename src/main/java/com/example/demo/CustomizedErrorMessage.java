package com.example.demo;

public class CustomizedErrorMessage {

    private String statusCode;
    private String errorMessage;

    public CustomizedErrorMessage(Integer statusCode, String errorMessage) {
        this.statusCode = statusCode.toString();
        this.errorMessage = errorMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
