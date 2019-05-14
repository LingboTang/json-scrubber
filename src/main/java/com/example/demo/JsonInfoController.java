package com.example.demo;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import javax.validation.ValidationException;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class JsonInfoController {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONENUMBER_REGEX =
            Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", Pattern.CASE_INSENSITIVE);

    public final static String DATE_FORMAT = "yyyy-MM-dd";


    @RequestMapping(value="/scrub_json", method=RequestMethod.POST, consumes="application/json", produces="application/json")
    public ResponseEntity<BusinessData> scrubJsonInfo(@RequestBody Map<String, String> body) throws Exception {

        // Initialize fields which needs to go into the scrubbing process.
        String firstName = "";
        String lastName = "";
        String email = "";
        String passWord = "";
        String phoneNumber = "";
        String dateOfBirth = "";
        String errorMessage = "";

        // Parsing the json by a Map
        for (Map.Entry<String, String> entry: body.entrySet()) {
            if (entry.getKey() == "firstName") {
                if(!Pattern.matches(".*[a-zA-Z]+.*", entry.getValue())) {
                    errorMessage = "Invalid First Name!";
                    throw new ValidationException(errorMessage);
                }
                else {
                    firstName = entry.getValue();
                }
            }
            else if (entry.getKey() == "lastName") {
                if(!Pattern.matches(".*[a-zA-Z]+.*", entry.getValue())) {
                    errorMessage = "Invalid Last Name!";
                    throw new ValidationException(errorMessage);
                }
                else {
                    lastName = entry.getValue();
                }
            }
            else if (entry.getKey() == "email") {
                if (!validateEmail(entry.getValue())) {
                    errorMessage = "Invalid email!";
                    throw new ValidationException(errorMessage);
                }
                else {
                    email = entry.getValue();
                }
            }
            else if (entry.getKey() == "password") {
                if (!validatePassword(entry.getValue())) {
                    errorMessage = "Invalid password!";
                    throw new ValidationException(errorMessage);
                }
                else {
                    passWord = entry.getValue();
                }
            }
            else if (entry.getKey() == "phoneNumber") {
                if (!validatePhoneNumnber(entry.getValue())) {
                    errorMessage = "Invalid Phone Number!";
                    throw new ValidationException(errorMessage);
                }
                else {
                    phoneNumber = entry.getValue();
                }
            }
            else if (entry.getKey() == "dateOfBirth") {
                if (!validateDateOfBirth(entry.getValue())) {
                    errorMessage = "Invalid Date of Birth!";
                    throw new ValidationException(errorMessage);
                }
                dateOfBirth = entry.getValue();
            }
            else {
                errorMessage = entry.getKey() + " is invalid";
                throw new ValidationException(errorMessage);
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

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean validatePassword(String pwdStr) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pwdStr);
        return matcher.find();
    }

    public static boolean validatePhoneNumnber(String phoneNumberStr) {
        Matcher matcher = VALID_PHONENUMBER_REGEX.matcher(phoneNumberStr);
        return matcher.find();
    }

    public static boolean validateDateOfBirth(String dateOfBirth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(dateOfBirth, new ParsePosition(0)) != null;
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
