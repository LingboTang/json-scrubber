package com.example.demo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@RestController
public class JsonInfoController {

    @RequestMapping(value="/scrub_json", method= RequestMethod.POST, consumes="application/json", produces="application/json")
    public String scrubJsonInfo(@RequestBody Map<String, String> body) {
        String firstName = "";
        String lastName = "";
        String email = "";
        String passWord = "";
        String phoneNumber = "";
        Date dateOfBirth = new Date();
        for (Map.Entry<String, String> entry: body.entrySet()) {
            if (entry.getKey() == "first_name") {
                firstName = entry.getValue();
            }
            else if (entry.getKey() == "last_name") {
                lastName = entry.getValue();
            }
            else if (entry.getKey() == "email") {
                email = entry.getValue();
            }
            else if (entry.getKey() == "password") {
                passWord = entry.getValue();
            }
            else if (entry.getKey() == "phone_number") {
                phoneNumber = entry.getValue();
            }
            else if (entry.getKey() == "date_of_birth") {
                String dateOfBirthString = entry.getValue();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                ParsePosition position = new ParsePosition(0);
                dateOfBirth = formatter.parse(dateOfBirthString, position);
            }
        }
        BusinessData businessData = new BusinessData(firstName, lastName, email, passWord, phoneNumber, dateOfBirth);
        JsonInfo jsonInfo = new JsonInfo();
        Gson gson = new Gson();
        ZonedDateTime logDate = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss.SSSSSS Z");
        String formattedTimeString = logDate.format(formatter);
        String jsonMetadata = gson.toJson(businessData);
        return jsonMetadata;
    }

}
