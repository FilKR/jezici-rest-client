package org.example;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<Question> questions = QuestionService.getInstance()
                    .getQuestions("3076dbc4-aca5-4073-9e58-1e72587f4170");
            questions.stream().map(Question::getText).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}