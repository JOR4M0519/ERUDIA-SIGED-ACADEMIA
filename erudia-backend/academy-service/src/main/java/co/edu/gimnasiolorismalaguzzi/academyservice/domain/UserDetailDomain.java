package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailDomain {
    private Integer id;
    private UserDomain user; // Asegúrate de que esta clase también esté definida correctamente
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String dni;
    private IdTypeDomain idType; // Asegúrate de que esta clase también esté definida correctamente
    private String neighborhood;
    private String city;
    private String positionJob;
}
