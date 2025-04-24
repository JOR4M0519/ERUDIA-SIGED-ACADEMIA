package co.edu.gimnasiolorismalaguzzi.eurekaservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**", "/actuator/**")) // Añadir /actuator/**
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/").authenticated()
                        .requestMatchers("/eureka/**").authenticated()
                        .anyRequest().denyAll() // Protege /eureka con autenticación
                         // Permite acceso a todas las demás rutas sin autenticación
                )
                .httpBasic(); // Usa autenticación básica para proteger el acceso a Eureka

        return httpSecurity.build();
    }

    /**
     * Configuración de CORS (Cross-Origin Resource Sharing).
     * Permite solicitudes desde un origen específico y define las políticas de seguridad para CORS.
     *
     * @return Una instancia de CorsConfigurationSource con las reglas de CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Orígenes permitidos
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://35.198.2.89:8181",
                "http://35.198.2.89:5173",
                "http://35.198.2.89",
                "http://35.198.2.89:80"
        ));
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept"
        ));
        // Métodos permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        configuration.setAllowCredentials(true);
        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
