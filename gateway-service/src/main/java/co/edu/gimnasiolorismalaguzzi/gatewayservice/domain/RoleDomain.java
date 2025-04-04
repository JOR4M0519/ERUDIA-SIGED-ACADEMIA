package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDomain {
    private Integer id;
    private String roleName;
    private Boolean status;
}
