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

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private JsonInfoController jsonInfoController;

    @Test
    public void scrubJsonInfoAPITest() throws Exception {
        String dateString = "1990-01-01";
        Date testDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        String validRequestJson = "{\"firstName\": \"Test\", \"lastName\": \"Test\", \"email\": \"test@test.org\", \"password\": \"PAAssWord\", \"phoneNumber\": \"780-000-0000\", \"dateOfBirth\": \"1991-01-01\"}";
        new MockServerClient("127.0.0.1", 1080).when(
                request()
                        .withMethod("POST")
                        .withPath("/scrub_json")
                        .withHeader("\"Content-type\", \"application/json\"")
                        .withHeader("\"Accept\", \"application/json\"")
                        .withBody(validRequestJson),
                         Times.exactly(1)).respond(
                response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"),
                                new Header("Cache-Control", "public, max-age=86400"))
                        .withBody("{ message: 'incorrect username and password combination' }")
                        .withDelay(TimeUnit.SECONDS,1)
        );
    }

    public static String asJsonString(final Object obj) {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(obj);
            return jsonString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public void scrubJsonInfo() throws Exception {

        File file = new File("output.txt");
        Scanner sc = new Scanner(file);
        List<String> lineContent = new ArrayList<String>();

        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
            lineContent.add(currentLine);
        }

        for (String line: lineContent) {
            System.out.println(line);
        }


    }*/

}