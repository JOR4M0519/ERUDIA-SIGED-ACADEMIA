package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.IdType;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user_detail")
public class UserDetail1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_detail_id_gen")
    @SequenceGenerator(name = "user_detail_id_gen", sequenceName = "user_detail_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 40)
    @Column(name = "first_name", length = 40)
    private String firstName;

    @Size(max = 40)
    @Column(name = "middle_name", length = 40)
    private String middleName;

    @Size(max = 40)
    @Column(name = "last_name", length = 40)
    private String lastName;

    @Size(max = 40)
    @Column(name = "second_last_name", length = 40)
    private String secondLastName;

    @Size(max = 30)
    @NotNull
    @Column(name = "address", nullable = false, length = 30)
    private String address;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Size(max = 20)
    @NotNull
    @Column(name = "dni", nullable = false, length = 20)
    private String dni;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_type_id", nullable = false)
    private IdType idType;

    @Size(max = 20)
    @NotNull
    @Column(name = "neighborhood", nullable = false, length = 20)
    private String neighborhood;

    @Size(max = 20)
    @NotNull
    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Size(max = 40)
    @Column(name = "position_job", length = 40)
    private String positionJob;

}