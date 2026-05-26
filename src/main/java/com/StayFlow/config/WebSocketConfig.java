package com.StayFlow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un "canal de radio" al que el Frontend se va a suscribir
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que el Frontend envía al Backend
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // El punto de entrada para establecer la conexión inicial
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Permite conexión desde React
                .withSockJS(); // Soporte para navegadores antiguos o bloqueos de red
    }
}