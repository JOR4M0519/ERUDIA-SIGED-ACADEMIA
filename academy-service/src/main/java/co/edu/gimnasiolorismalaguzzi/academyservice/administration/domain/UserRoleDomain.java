package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Role;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleDomain {
    private Integer id;
    private User user;
    private Role role;
}
