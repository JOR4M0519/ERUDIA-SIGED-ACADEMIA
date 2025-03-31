package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDomain implements Serializable {
    private Integer id;
    private String uuid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<UserRoleDomain> roles;
    private String status;
    private String promotionStatus;

    public UserDomain(Integer id) {
        this.id = id;
    }
}
