
package co.edu.gimnasiolorismalaguzzi.gatewayservice.config.filter;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.config.JwtAuthenticationConverter;
import co.edu.gimnasiolorismalaguzzi.gatewayservice.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtClaimsFilter implements WebFilter {

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;


/**
 * Filtro que intercepta las solicitudes para extraer, validar y propagar información de autenticación
 * y roles desde un token JWT. Configura el SecurityContext para garantizar la seguridad en las
 * solicitudes que pasan por este filtro.
 *
 * @param exchange El contexto del intercambio de la solicitud y respuesta HTTP.
 * @param chain    La cadena de filtros reactivos que continúa el procesamiento de la solicitud.
 * @return Un objeto `Mono<Void>` que indica la finalización del procesamiento del filtro.
 */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        if (path.startsWith("/eureka") || path.contains("/gtw/public") || path.contains("/actuator/health")) {
            return chain.filter(exchange);
        }

        // Extrae el token JWT de la cabecera Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Eliminar el prefijo "Bearer " del token
            Jwt jwt = decodeJwt(token); // Decodificar el JWT utilizando un método personalizado

            // Convierte el JWT en un objeto de autenticación
            AbstractAuthenticationToken authenticationToken = jwtAuthenticationConverter.convert(jwt);

            // Extrae los roles desde las autoridades del token
            //String roles = authenticationToken.getAuthorities().stream()
            String roles = authenticationToken.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> authority.startsWith("ROLE_")) // Filtra solo los roles
                    .collect(Collectors.joining(","));

            // Agrega roles y el usuario como cabeceras en el ServerWebExchange
            exchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header("X-Roles", roles) // Cabecera con los roles
                            .header("X-User", jwt.getClaimAsString("sub")) // Cabecera con el ID del usuario
                            .build())
                    .build();

            // Configura el SecurityContext con los roles y claims extraídos
            return exchange.getPrincipal()
                    .filter(principal -> principal instanceof JwtAuthenticationToken)
                    .map(principal -> (JwtAuthenticationToken) principal)
                    .map(jwtAuthenticationToken -> {
                        // Extraer roles y claims del JWT
                        List<GrantedAuthority> authorities = authenticationToken.getAuthorities()
                                .stream()
                                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                                .collect(Collectors.toList());

                        String username = jwtAuthenticationToken.getName(); // Obtiene el nombre del usuario

                        // Crea un token de autenticación y lo configura en el SecurityContext
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        return authentication;
                    })
                    .then(chain.filter(exchange)); // Continúa con la cadena de filtros
        }

        // Si no hay un token válido, lanza una excepción personalizada
        throw new AppException("Invalid Token Access", HttpStatus.UNAUTHORIZED);
    }


/**
 * Método para decodificar un token JWT.
 *
 * @param token El token JWT a decodificar.
 * @return Un objeto `Jwt` que representa el token decodificado.
 * @throws RuntimeException Si el token es inválido o no se puede decodificar.
 */

    private Jwt decodeJwt(String token) {

        // Crea un JwtDecoder usando NimbusJwtDecoder con un URI de conjunto de claves públicas (JWK)
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        try {
            // Decodifica el JWT
            return jwtDecoder.decode(token);

        } catch (JwtException ex) {
            throw new AppException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

}


