package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "institution")
public class Institution1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "institution_id_gen")
    @SequenceGenerator(name = "institution_id_gen", sequenceName = "institution_id_seq", allocationSize = 1)
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
    @ColumnDefault("now()")
    @Column(name = "incorporation_date", nullable = false)
    private LocalDate incorporationDate;

}