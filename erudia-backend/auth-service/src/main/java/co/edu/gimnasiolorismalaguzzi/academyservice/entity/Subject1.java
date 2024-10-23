package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subject")
public class Subject1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_id_gen")
    @SequenceGenerator(name = "subject_id_gen", sequenceName = "subject_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "subject_name", nullable = false, length = 50)
    private String subjectName;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

}