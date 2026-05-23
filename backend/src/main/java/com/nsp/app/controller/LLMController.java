package com.nsp.app.controller;

import com.nsp.app.model.AskRequest;
import com.nsp.app.model.AskResponse;
import com.nsp.app.service.LLMService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LLMController {

    private static final Logger log = LoggerFactory.getLogger(LLMController.class);

    private final LLMService llmService;

    public LLMController(LLMService llmService) {
        this.llmService = llmService;
    }

    /**
     * POST /api/ask
     * Main endpoint — accepts a question and returns LLM answer
     */
    @PostMapping("/ask")
    public ResponseEntity<AskResponse> ask(@Valid @RequestBody AskRequest request) {
        log.info("Received question: {}", request.getQuestion());
        long startTime = System.currentTimeMillis();

        try {
            String answer = llmService.askLLM(request.getQuestion(), request.getContext());
            long elapsed = System.currentTimeMillis() - startTime;

            AskResponse response = AskResponse.builder()
                    .question(request.getQuestion())
                    .answer(answer)
                    .model(llmService.getModelName())
                    .timestamp(LocalDateTime.now())
                    .responseTimeMs(elapsed)
                    .success(true)
                    .build();

            log.info("Answered in {}ms", elapsed);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error calling LLM", e);
            long elapsed = System.currentTimeMillis() - startTime;

            AskResponse errorResponse = AskResponse.builder()
                    .question(request.getQuestion())
                    .answer(null)
                    .timestamp(LocalDateTime.now())
                    .responseTimeMs(elapsed)
                    .success(false)
                    .errorMessage("Failed to get response from LLM: " + e.getMessage())
                    .build();

            return ResponseEntity.status(503).body(errorResponse);
        }
    }

    /**
     * GET /api/health
     * Health check endpoint (used by CI/CD pipelines)
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "app", "NSP-4-S2-S25App",
                "model", llmService.getModelName(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
