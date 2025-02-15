package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.security;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    /**
     * Este método es llamado cuando un usuario intenta acceder a un recurso protegido sin la autenticación necesaria.
     * Configura la respuesta HTTP para indicar que el acceso no está autorizado y proporciona un cuerpo de respuesta
     * en formato JSON que contiene un mensaje de error.
     * @param request La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se enviará al cliente.
     * @param authException La excepción de autenticación que desencadenó la llamada a este método.
     * @throws IOException Si ocurre un error durante la escritura de la respuesta.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     *
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Unauthorize path"));
    }
}
