package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class IdTypeDomain implements Serializable {
    private Integer id; // o el tipo correspondiente
    private String name; // o cualquier otro atributo que necesites
    // Agrega otros atributos según sea necesario
}
