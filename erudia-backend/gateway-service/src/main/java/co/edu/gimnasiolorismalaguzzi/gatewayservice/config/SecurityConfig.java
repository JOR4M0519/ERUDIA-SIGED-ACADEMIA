package co.edu.gimnasiolorismalaguzzi.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/eureka/**").build();
    }*/

    //@Autowired
    //private final JwtAuthenticationConverter jwtAuthenticationConverter;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF
                .authorizeExchange(exchange -> exchange
                        .pathMatchers( "/api/login/**").permitAll() // Permitir rutas específicas
                        .pathMatchers("/eureka/**").authenticated()
                        .anyExchange().authenticated() // Resto requiere autenticación
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.addAllowedOrigin("http://localhost:5173"); // Origen permitido
                    configuration.addAllowedHeader("*"); // Todos los encabezados permitidos
                    configuration.addAllowedMethod("*"); // Todos los métodos permitidos
                    configuration.setAllowCredentials(true); // Permitir credenciales
                    return configuration;
                }))
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt); // Configuración de JWT

        return http.build();
    }

    /*
    @Bean
    //public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                .csrf(csrf -> csrf.disable())// (csrf -> csrf.disable())
                .authorizeHttpRequests(http -> http
                        .requestMatchers("/eureka/**","/api/login/**")
                        .permitAll())
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /*
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)// (csrf -> csrf.disabled)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));*/
    //return httpSecurity.build();
    //}

    /*
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.csrf()
                .disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        return serverHttpSecurity.build();

    }*/


  /*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/eureka/**").permitAll()
                        .anyRequest()
                        .authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();

    }*/
}
