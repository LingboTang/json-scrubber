package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.scanner.ScannerImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(JsonInfoController.class)
public class JsonInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JsonInfoController jsonInfoController;

    public static String asJsonString(final Object obj) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(obj);
            return jsonString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void scrubJsonInfoAPITest() throws Exception {
        String dateString = "1990-01-01";
        Date testDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        String validRequestJson = "{\"firstName\": \"Test\", \"lastName\": \"Test\", \"email\": \"test@test.org\", \"password\": \"PAAssWord\", \"phoneNumber\": \"780-000-0000\", \"dateOfBirth\": \"1991-01-01\"}";
    }

    @Test
    public void writeToFileTest() throws Exception {

        String filePath = "output.txt";

        File file = new File(filePath);

        JsonInfo jsonInfo = new JsonInfo();

        ZonedDateTime logDate = jsonInfo.getLogDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss.SSSSSS Z");
        String formattedTimeString = logDate.format(formatter);

        BusinessData businessData = new BusinessData("Test", "Test", "test@test.org", "PAAssWord", "780-000-0000", "1991-01-01");

        JsonInfoController.writeToFile(filePath, jsonInfo, formattedTimeString, businessData);

        Scanner sc = new Scanner(file);
        List<String> lineContent = new ArrayList<String>();

        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
            lineContent.add(currentLine);
        }

        assertEquals(lineContent.size(), 6);

        for (int i = 1; i < lineContent.size()-1; i++) {
            String[] mapPair = lineContent.get(i).split(": ");
            for (int j = 0; j < mapPair.length; j++) {
                String key = mapPair[0];
                String value = mapPair[1];
                if (i == 1) {
                    assertEquals(key, "firstName");
                    assertEquals(value, "Test");
                }
                else if (i == 2) {
                    assertEquals(key, "email");
                    assertEquals(value, "test@test.org");
                }
                else if (i == 3) {
                    assertEquals(key, "password");
                    assertEquals(value, "PAAssWord");
                }
                else if (i == 4) {
                    assertEquals(key, "phoneNumber");
                    assertEquals(value, "780-000-0000");
                }
            }
        }


    }

}