package com.nsp.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskResponse {

    private String question;
    private String answer;
    private String model;
    private LocalDateTime timestamp;
    private long responseTimeMs;
    private boolean success;
    private String errorMessage;
}
