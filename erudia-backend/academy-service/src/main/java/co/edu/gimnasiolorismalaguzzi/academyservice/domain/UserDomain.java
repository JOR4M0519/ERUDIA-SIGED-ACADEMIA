package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class UserDomain implements Serializable {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;

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
