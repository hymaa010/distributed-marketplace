package org.team13.marketplace.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.team13.marketplace.dto.item.ProductEnrichmentRequest;

@Service
public class AIService {
    private final ChatClient chatClient;

    public AIService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("""
                        You are a professional e-commerce copywriter.
                        Your task is to provide a single, compelling product description based on the input.
                        
                        CRITICAL INSTRUCTIONS:
                        1. Output ONLY the description text.
                        2. Do NOT provide multiple options.
                        3. Do NOT include introductory phrases like "Here is a description" or "Sure, I can help."
                        4. Do NOT include any markdown headers or labels.
                        5. Return only the final copy.
                        """)
                .build();
    }

    public String enrichProduct(ProductEnrichmentRequest r) {
        return chatClient.prompt()
                .user(u -> u.text("Product Name: {name}\nBrand: {brand}\nReturn only the raw description text.")
                        .param("name", r.getName())
                        .param("brand", r.getBrand()))
                .call()
                .content();
    }
}
