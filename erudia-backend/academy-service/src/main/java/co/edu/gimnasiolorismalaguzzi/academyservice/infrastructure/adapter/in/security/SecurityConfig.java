package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.security;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.security.filter.SecurityContextFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //@Autowired
    //private JwtAuthenticationConverter jwtAuthenticationConverter;
    private final SecurityContextFilter securityContextFilter; // Inyecta el filtro aquí

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                //Deshabilitación a protección de intercepción por medio de formularios
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {
                    //Habilitar Rutas
                    authorize.requestMatchers("/api/academy/public/**").permitAll();
                    //Rutas Aseguradas
                    authorize.anyRequest().authenticated();
                })
                .addFilterBefore(securityContextFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                //Funcionamiento por medio de OAuth2
                /*.oauth2ResourceServer(oauth2 ->

                        //Configuración del Token
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                )*/
                // oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
                //Gestión de Sesion
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                        /*

                        //Se evita el ataque de Fijacion de sesion creando otra
                        .sessionFixation()
                            .migrateSession()

                        .invalidSessionUrl("http://localhost:8080")  //Redireccionamiento failed login
                        .maximumSessions(1)
                        .expiredUrl("http://localhost:8080")         //Redireccionamiento cuando se expira el tiempo de la sesion
                );*/

                //Redireccionamiento de ingreso al login
                /*.formLogin(form -> form
                        .successHandler(successHandler())
                        .permitAll());*/
        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler(){
        return (((request, response, authentication) -> {
            response.sendRedirect("/index");
        }));
    }

}
