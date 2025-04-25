package co.edu.gimnasiolorismalaguzzi.gatewayservice.domain;

import co.edu.gimnasiolorismalaguzzi.gatewayservice.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
