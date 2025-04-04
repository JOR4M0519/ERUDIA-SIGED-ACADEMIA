package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserRoleDomain implements Serializable {
    private Integer id;
    // En lugar de incluir el objeto User completo, solo incluye su ID
    private Integer userId;
    private RoleDomain role;
}
