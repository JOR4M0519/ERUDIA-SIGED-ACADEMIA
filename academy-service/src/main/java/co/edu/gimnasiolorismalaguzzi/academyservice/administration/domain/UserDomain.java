package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@RequiredArgsConstructor
@Data
public class UserDomain implements Serializable {
    private Integer id;
    private String uuid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
    private String status;

    public UserDomain(Integer id) {
        this.id = id;
    }
}
