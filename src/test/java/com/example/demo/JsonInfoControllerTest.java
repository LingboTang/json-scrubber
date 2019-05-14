package com.example.demo;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(JsonInfoController.class)
public class JsonInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JsonInfoController jsonInfoController;

    @Test
    public void scrubJsonInfoAPITest() throws Exception {
        String dateString = "1990-01-01";
        Date testDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        mvc.perform(MockMvcRequestBuilders.post("/scrub_json")
                .content(asJsonString(new BusinessData("Test", "Test", "test@test.org", "PAAssWord", "780-000-0000", "1990-01-01")))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Test"));
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