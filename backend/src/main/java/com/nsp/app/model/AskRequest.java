package com.nsp.app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AskRequest {

    @NotBlank(message = "Question cannot be blank")
    @Size(min = 3, max = 500, message = "Question must be between 3 and 500 characters")
    private String question;

    private String context;

    public AskRequest() {}

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }
}