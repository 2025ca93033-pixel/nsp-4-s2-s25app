package com.nsp.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class LLMService {

    private static final Logger log = LoggerFactory.getLogger(LLMService.class);

    @Value("${huggingface.api.token}")
    private String hfApiToken;

    @Value("${huggingface.api.model:google/flan-t5-large}")
    private String modelName;

    @Value("${huggingface.api.base-url:https://api-inference.huggingface.co/models}")
    private String baseUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LLMService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sends a question to Hugging Face LLM and returns the answer.
     *
     * @param question  the user's question
     * @param context   optional context/instruction
     * @return generated answer from the LLM
     */
    public String askLLM(String question, String context) throws Exception {
        String prompt = buildPrompt(question, context);

        Map<String, Object> payload = new HashMap<>();
        payload.put("inputs", prompt);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_new_tokens", 200);
        parameters.put("temperature", 0.7);
        parameters.put("do_sample", true);
        payload.put("parameters", parameters);

        String jsonBody = objectMapper.writeValueAsString(payload);
        String apiUrl = baseUrl + "/" + modelName;

        log.info("Calling HF API: {} | Prompt: {}", apiUrl, prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + hfApiToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(60))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        log.info("HF API response status: {}", response.statusCode());

        if (response.statusCode() == 503) {
            // Model is loading, common with free tier
            return "The AI model is warming up. Please try again in 20-30 seconds.";
        }

        if (response.statusCode() != 200) {
            log.error("HF API error: {} - {}", response.statusCode(), response.body());
            throw new RuntimeException("LLM API returned error: " + response.statusCode());
        }

        return parseResponse(response.body());
    }

    private String buildPrompt(String question, String context) {
        if (context != null && !context.isBlank()) {
            return context + "\n\nQuestion: " + question + "\nAnswer:";
        }
        return "Answer the following question clearly and concisely.\n\nQuestion: " + question + "\nAnswer:";
    }

    private String parseResponse(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);

        // Hugging Face text generation returns an array
        if (root.isArray() && root.size() > 0) {
            JsonNode first = root.get(0);
            if (first.has("generated_text")) {
                String fullText = first.get("generated_text").asText();
                // Extract only the answer part (after "Answer:")
                if (fullText.contains("Answer:")) {
                    return fullText.substring(fullText.lastIndexOf("Answer:") + 7).trim();
                }
                return fullText.trim();
            }
        }

        // Fallback for summarization models
        if (root.isArray() && root.get(0).has("summary_text")) {
            return root.get(0).get("summary_text").asText().trim();
        }

        log.warn("Unexpected response format: {}", responseBody);
        return "Received a response but could not parse it. Raw: " + responseBody;
    }

    public String getModelName() {
        return modelName;
    }
}
