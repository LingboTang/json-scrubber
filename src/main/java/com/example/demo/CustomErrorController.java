package com.example.demo;

import com.google.gson.Gson;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        CustomizedErrorMessage customizedErrorMessage = new CustomizedErrorMessage(statusCode, exception.getMessage());

        Gson gson = new Gson();

        return gson.toJson(customizedErrorMessage);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}