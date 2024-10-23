package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class UserDomain {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastLogin;
    private Integer attemptedFailedLogin;
    private String status;
}
