/*
package co.edu.gimnasiolorismalaguzzi.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:5173"); // Cambia según el origen de tu frontend
        corsConfig.addAllowedHeader("*"); // Permite todos los encabezados
        corsConfig.addAllowedMethod("*"); // Permite todos los métodos
        corsConfig.setAllowCredentials(true); // Habilita el envío de credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Aplica configuración globalmente

        return new CorsWebFilter(source);
    }



}
*/
