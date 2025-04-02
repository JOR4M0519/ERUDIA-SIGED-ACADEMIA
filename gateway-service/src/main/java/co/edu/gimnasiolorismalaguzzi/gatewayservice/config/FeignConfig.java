package co.edu.gimnasiolorismalaguzzi.gatewayservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.optionals.OptionalDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Collectors;

@Configuration
public class FeignConfig {

    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the Java 8 date/time module
        objectMapper.registerModule(new JavaTimeModule());
        // Configure to use string timestamps instead of arrays/numeric timestamps
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper objectMapper) {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new OptionalDecoder(new JacksonDecoder(objectMapper));
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // Enable detailed logging for debugging
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Always set Content-Type header to application/json for all requests
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                // Get the current authenticated user
                String username = authentication.getName();

                // Extract roles
                String roles = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

                // Add the headers that SecurityContextFilter expects
                requestTemplate.header("X-User", username);
                requestTemplate.header("X-Roles", roles);
            } else {
                // Fallback for service-to-service calls with no active user context
                requestTemplate.header("X-User", "service-account");
                requestTemplate.header("X-Roles", "ROLE_SERVICE");
            }
        };
    }
}