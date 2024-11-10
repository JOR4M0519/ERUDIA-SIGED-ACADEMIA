package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailDomain {
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
}
