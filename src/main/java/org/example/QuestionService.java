package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class QuestionService {
    private static QuestionService instance;

    private final HttpClient client;
    private final ObjectMapper mapper;

    private QuestionService() {
        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Question> getQuestions(String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://rasa.singidunum.ac.rs/api/question/category/upis"))
                .header("Content-Type", "application/json")
                .header("x-token", token)
                .GET()
                .build();

        HttpResponse<String> json = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(json.body(), new TypeReference<List<Question>>(){});
    }

    public static QuestionService getInstance() {
        if (instance == null)
            instance = new QuestionService();

        return instance;
    }
}
