package co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class InstitutionDomain implements Serializable {
    private Integer id;
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String email;
    private String city;
    private String department;
    private String country;
    private String postalCode;
    private String legalRepresentative; //representante legal
    private LocalDate incorporationDate;

}
