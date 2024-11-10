package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.security;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Maneja excepciones del tipo {@link AppException} y las convierte en respuestas HTTP con un cuerpo
     * que contiene detalles del error.
     * @param ex La excepción {@link AppException} que se captura.
     * @return Una instancia de {@link ResponseEntity} con un objeto {@link ErrorDto} que contiene el mensaje de error.
     */
    @ExceptionHandler(value = {AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handledException(AppException ex){
        return ResponseEntity.status(ex.getCode())
                .body(ErrorDto.builder().message(ex.getMessage()).build());
    }
}
