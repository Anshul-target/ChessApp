package com.chessClone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // For development - allow all patterns
        registry.addEndpoint("/chess-websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        registry.addEndpoint("/chess-websocket")
                .setAllowedOriginPatterns("*");

        // For production - specify exact domains
        /*
        registry.addEndpoint("/chess-websocket")
                .setAllowedOrigins(
                    "https://chessapp-production.up.railway.app",
                    "http://localhost:8080",
                    "http://localhost:3000"
                )
                .setAllowCredentials(false) // Disable credentials if you don't need them
                .withSockJS();

        registry.addEndpoint("/chess-websocket")
                .setAllowedOrigins(
                    "https://chessapp-production.up.railway.app",
                    "http://localhost:8080",
                    "http://localhost:3000"
                )
                .setAllowCredentials(false);
        */
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory message broker
        registry.enableSimpleBroker("/topic", "/queue");

        // Set the application destination prefix
        registry.setApplicationDestinationPrefixes("/app");
    }
}