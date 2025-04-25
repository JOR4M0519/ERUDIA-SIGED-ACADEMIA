package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Relationship;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDetailDomain implements Serializable {
    private Integer id;
    private UserDomain user;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String dni;
    private IdTypeDomain idType;
    private String neighborhood;
    private String city;
    private String positionJob;

    private List<User> relatives;  //Campo externo
}


