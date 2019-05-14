package com.example.demo;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

import java.io.*;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;


@RestController
public class JsonInfoController {

    @RequestMapping(value="/scrub_json", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<BusinessData> scrubJsonInfo(@RequestBody Map<String, String> body) throws JSONException {

        // Initialize fields which needs to go into the scrubbing process.
        String firstName = "";
        String lastName = "";
        String email = "";
        String passWord = "";
        String phoneNumber = "";
        String dateOfBirth = "";

        // Parsing the json by a Map
        for (Map.Entry<String, String> entry: body.entrySet()) {
            if (entry.getKey() == "firstName") {
                firstName = entry.getValue();
            }
            else if (entry.getKey() == "lastName") {
                lastName = entry.getValue();
            }
            else if (entry.getKey() == "email") {
                email = entry.getValue();
            }
            else if (entry.getKey() == "password") {
                passWord = entry.getValue();
            }
            else if (entry.getKey() == "phoneNumber") {
                phoneNumber = entry.getValue();
            }
            else if (entry.getKey() == "dateOfBirth") {
                dateOfBirth = entry.getValue();
            }
        }

        // Construct Business data object
        BusinessData businessData = new BusinessData(firstName, lastName, email, passWord, phoneNumber, dateOfBirth);

        // Construct Json Metadata (for logging)
        JsonInfo jsonInfo = new JsonInfo();

        // Format time
        ZonedDateTime logDate = jsonInfo.getLogDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss.SSSSSS Z");
        String formattedTimeString = logDate.format(formatter);

        String dirPath = "output.txt";

        writeToFile(dirPath, jsonInfo, formattedTimeString, businessData);

        return new ResponseEntity<BusinessData>(businessData, HttpStatus.OK);
    }

    private static String scrubbingBusinessData(BusinessData businessData) {
        String scrubbedText = "";
        String scrubbedFirstName = "firstName" + ": " + businessData.getFirstName() + "\n";
        String scrubbedEmail = "email" + ": " + businessData.getEmail() + "\n";
        String scrubbedPassword = "password" + ": "  + businessData.getPassword() + "\n";
        String scrubbedPhoneNumber = "phoneNumber" + ": " + businessData.getPhoneNumber() + "\n";
        scrubbedText = scrubbedText + scrubbedFirstName + scrubbedEmail + scrubbedPassword + scrubbedPhoneNumber + "\n";
        return scrubbedText;
    }

    public static void writeToFile(String filePath, JsonInfo jsonInfo, String formattedTimeString, BusinessData businessData) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"));
            String metadataLine = jsonInfo.getUUID() + " " + formattedTimeString + "\n";
            writer.write(metadataLine);
            String scrubbedBusinessData = scrubbingBusinessData(businessData);
            writer.write(scrubbedBusinessData);


        } catch (IOException ex) {
            // Report
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            }
            catch (Exception ex) {
                /*ignore*/
            }
        }
    }

}
