package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

/*
    private Integer id;
    private String username;
    private String email;
    private String password;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
    private Integer attemptedFailedLogin;
    private String status;*/

}
