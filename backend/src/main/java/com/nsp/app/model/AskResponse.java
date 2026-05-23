package com.nsp.app.model;

import java.time.LocalDateTime;

public class AskResponse {

    private String question;
    private String answer;
    private String model;
    private LocalDateTime timestamp;
    private long responseTimeMs;
    private boolean success;
    private String errorMessage;

    public AskResponse() {}

    // Getters
    public String getQuestion()        { return question; }
    public String getAnswer()          { return answer; }
    public String getModel()           { return model; }
    public LocalDateTime getTimestamp(){ return timestamp; }
    public long getResponseTimeMs()    { return responseTimeMs; }
    public boolean isSuccess()         { return success; }
    public String getErrorMessage()    { return errorMessage; }

    // Setters
    public void setQuestion(String question)            { this.question = question; }
    public void setAnswer(String answer)                { this.answer = answer; }
    public void setModel(String model)                  { this.model = model; }
    public void setTimestamp(LocalDateTime timestamp)   { this.timestamp = timestamp; }
    public void setResponseTimeMs(long responseTimeMs)  { this.responseTimeMs = responseTimeMs; }
    public void setSuccess(boolean success)             { this.success = success; }
    public void setErrorMessage(String errorMessage)    { this.errorMessage = errorMessage; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final AskResponse obj = new AskResponse();

        public Builder question(String v)          { obj.question = v;        return this; }
        public Builder answer(String v)            { obj.answer = v;          return this; }
        public Builder model(String v)             { obj.model = v;           return this; }
        public Builder timestamp(LocalDateTime v)  { obj.timestamp = v;       return this; }
        public Builder responseTimeMs(long v)      { obj.responseTimeMs = v;  return this; }
        public Builder success(boolean v)          { obj.success = v;         return this; }
        public Builder errorMessage(String v)      { obj.errorMessage = v;    return this; }

        public AskResponse build()                 { return obj; }
    }
}