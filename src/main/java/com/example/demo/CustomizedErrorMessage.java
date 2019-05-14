package com.example.demo;

public class CustomizedErrorMessage {

    private String statusCode;
    private Exception errorMessage;

    public CustomizedErrorMessage(Integer statusCode, Exception errorMessage) {
        this.statusCode = statusCode.toString();
        this.errorMessage = errorMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Exception getErrorMessage() {
        return errorMessage;
    }
}
