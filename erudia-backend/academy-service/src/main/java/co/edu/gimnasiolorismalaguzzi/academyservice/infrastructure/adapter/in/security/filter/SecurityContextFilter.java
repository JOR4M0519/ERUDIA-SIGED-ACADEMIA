package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.security.filter;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecurityContextFilter implements Filter {

    /**
     * Este método implementa un filtro que se ejecuta para cada solicitud HTTP.
     * Se encarga de extraer la información del usuario y roles de las cabeceras HTTP
     * y configurar el contexto de seguridad (SecurityContext) para la solicitud actual.
     *
     * @param request  La solicitud entrante, genérica como `ServletRequest`.
     * @param response La respuesta saliente, genérica como `ServletResponse`.
     * @param chain    La cadena de filtros que continúa el procesamiento de la solicitud.
     * @throws IOException      Si ocurre un error de entrada/salida durante el procesamiento.
     * @throws ServletException Si ocurre un error relacionado con el procesamiento del filtro.
     */
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request,
                         jakarta.servlet.ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // Convertir el objeto genérico ServletRequest a HttpServletRequest
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Obtener el usuario de la cabecera "X-User"
        String user = httpRequest.getHeader("X-User");

        // Obtener los roles de la cabecera "X-Roles"
        String rolesHeader = httpRequest.getHeader("X-Roles");

        // Si se encuentran las cabeceras "X-User" y "X-Roles"
        if (user != null && rolesHeader != null) {

            // Procesar la lista de roles separados por comas desde el encabezado "X-Roles"
            List<GrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(SimpleGrantedAuthority::new) // Convertir cada rol en un objeto SimpleGrantedAuthority
                    .collect(Collectors.toList());    // Recopilar en una lista de GrantedAuthority

            // Crear un objeto de autenticación con el usuario y los roles obtenidos
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            // Crear un nuevo contexto de seguridad para la solicitud actual
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);

            // Establecer el contexto de seguridad en el SecurityContextHolder
            SecurityContextHolder.setContext(securityContext);
        }

        // Continuar con la cadena de filtros para procesar la solicitud
        chain.doFilter(request, response);
    }

}