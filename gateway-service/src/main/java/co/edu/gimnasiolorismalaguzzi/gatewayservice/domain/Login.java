package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Login implements Serializable {
    private Integer id;
    private String uuid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String lastPassword;
    private Set<String> roles;
    private String status;


}
