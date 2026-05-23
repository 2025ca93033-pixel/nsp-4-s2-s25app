package com.nsp.app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AskRequest {

    @NotBlank(message = "Question cannot be blank")
    @Size(min = 3, max = 500, message = "Question must be between 3 and 500 characters")
    private String question;

    private String context; // optional context/system prompt
}
