package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "institution")
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Size(max = 15)
    @NotNull
    @Column(name = "nit", nullable = false, length = 15)
    private String nit;

    @NotNull
    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    private String address;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Size(max = 256)
    @NotNull
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @NotNull
    @Column(name = "city", nullable = false, length = Integer.MAX_VALUE)
    private String city;

    @NotNull
    @Column(name = "department", nullable = false, length = Integer.MAX_VALUE)
    private String department;

    @NotNull
    @Column(name = "country", nullable = false, length = Integer.MAX_VALUE)
    private String country;

    @Size(max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Size(max = 30)
    @NotNull
    @Column(name = "legal_representative", nullable = false, length = 30)
    private String legalRepresentative;

    @NotNull
    @Column(name = "incorporation_date", nullable = false)
    private LocalDate incorporationDate;

}