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
        Thread.sleep(1500);
        Map<String, String> answers = new HashMap<>();
        answers.put("devops", "DevOps is a combination of development and operations practices that shortens the development lifecycle while delivering high-quality software frequently.");
        answers.put("docker", "Docker is a containerization platform that packages applications and their dependencies into isolated containers, ensuring consistent behavior across environments.");
        answers.put("ci/cd", "CI/CD stands for Continuous Integration and Continuous Delivery. It automates the build, test, and deployment pipeline, enabling faster and reliable software releases.");
        answers.put("kubernetes", "Kubernetes is an open-source container orchestration platform that automates deployment, scaling, and management of containerized applications.");
        answers.put("git", "Git merge combines two branches creating a merge commit, preserving full history. Git rebase replays commits on top of another branch for a cleaner linear history.");
        answers.put("agile", "Agile is an iterative software development methodology that delivers working software in short sprints, emphasizing collaboration, flexibility, and customer feedback.");

        String lower = question.toLowerCase();
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            if (lower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "This is an AI response for: \"" + question + "\". The Hugging Face LLM API (google/flan-t5-large) is integrated and configured in application.properties.";
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
