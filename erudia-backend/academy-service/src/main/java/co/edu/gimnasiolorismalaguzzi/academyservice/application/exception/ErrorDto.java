package co.edu.gimnasiolorismalaguzzi.academyservice.application.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data

public class ErrorDto {
    private String message;
}
