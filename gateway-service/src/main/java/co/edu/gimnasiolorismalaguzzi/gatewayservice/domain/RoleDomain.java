package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDomain {
    private Integer id;
    private String roleName;
    private Boolean status;
}
