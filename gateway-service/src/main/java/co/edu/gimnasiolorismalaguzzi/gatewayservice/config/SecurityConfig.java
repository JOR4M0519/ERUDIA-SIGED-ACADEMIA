package co.edu.gimnasiolorismalaguzzi.gatewayservice.config;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.config.filter.JwtClaimsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

//@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtClaimsFilter jwtClaimsFilter;

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/eureka/**").build();
    }*/

    @Bean
    public SecurityWebFilterChain eurekaSecurityWebFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/eureka/**")) // Aplica esta configuración a /eureka/**
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Deshabilita CSRF para Eureka
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated() // Requiere autenticación para cualquier ruta de /eureka/**
                )
                .httpBasic(); // Habilita autenticación básica

        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
                .username("eureka")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    /**
     * Configuración de la seguridad para el servicio utilizando Spring Security.
     * Define las reglas de acceso, filtros y configuración de CORS.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
                // Deshabilita la protección contra ataques CSRF
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Configuración de autorizaciones para intercambios HTTP
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/gtw/public/**").permitAll() // Permitir acceso sin autenticación a rutas de login
                        //.pathMatchers("/eureka/**").authenticated() // Requiere autenticación para rutas de eureka
                        .anyExchange().authenticated() // Requiere autenticación para cualquier otra ruta
                )

                // Configuración de CORS (Cross-Origin Resource Sharing)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Agrega un filtro personalizado para manejar claims de JWT
                .addFilterAt(jwtClaimsFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                // Configuración del servidor OAuth2 como recurso para validar JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(reactiveJwtAuthenticationConverter())) // Convierte JWT a autenticación reactiva
                );

        return http.build(); // Construye y devuelve la cadena de filtros de seguridad
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
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://35.198.2.89:8181");// Permitir solicitudes desde este origen
        configuration.addAllowedHeader("*"); // Permitir todos los encabezados
        configuration.addAllowedMethod("*"); // Permitir todos los métodos HTTP
        configuration.setAllowCredentials(true); // Permitir el uso de credenciales (cookies, encabezados de autenticación)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar esta configuración a todas las rutas
        return source;
    }

    /**
     * Configura un convertidor reactivo para JWT.
     * Convierte un token JWT en una instancia de AbstractAuthenticationToken que será utilizada en el contexto de seguridad.
     *
     * @return Un convertidor reactivo de JWT a AbstractAuthenticationToken.
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        return new Converter<Jwt, Mono<AbstractAuthenticationToken>>() {
            @Override
            public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
                return Mono.justOrEmpty(delegate.convert(jwt)); // Convierte el JWT de forma reactiva
            }
        };
    }

    /**
     * Registro de sesiones para administrar sesiones concurrentes.
     * Es útil para gestionar el estado de las sesiones y prevenir múltiples sesiones por usuario.
     *
     * @return Una instancia de SessionRegistry para la administración de sesiones.
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    /*public AuthenticationSuccessHandler successHandler(){
        return (request, response, authentication) -> {
            response.sendRedirect("/index");
        };
    }*/
}