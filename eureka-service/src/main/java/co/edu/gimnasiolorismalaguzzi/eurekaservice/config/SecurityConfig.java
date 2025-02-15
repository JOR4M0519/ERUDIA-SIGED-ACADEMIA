package co.edu.gimnasiolorismalaguzzi.eurekaservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**")) // Deshabilita CSRF para Eureka
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").authenticated()
                        .requestMatchers("/eureka/**").authenticated()
                        .anyRequest().denyAll() // Protege /eureka con autenticación
                         // Permite acceso a todas las demás rutas sin autenticación
                )
                .httpBasic(); // Usa autenticación básica para proteger el acceso a Eureka

        return httpSecurity.build();
    }
}
