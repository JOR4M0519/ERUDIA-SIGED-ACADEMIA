package co.edu.gimnasiolorismalaguzzi.gatewayservice.config;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.util.KeycloakProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Bean
    public KeycloakProvider keycloakProvider() {
        return new KeycloakProvider();
    }
}
