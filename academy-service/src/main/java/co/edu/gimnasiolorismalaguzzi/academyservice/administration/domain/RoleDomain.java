package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RoleDomain {
    private Integer id;
    private String roleName;
    private Boolean status;
}
