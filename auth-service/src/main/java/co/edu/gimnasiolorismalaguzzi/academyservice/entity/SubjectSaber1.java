package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Sabere;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subject_saber")
public class SubjectSaber1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_saber_id_gen")
    @SequenceGenerator(name = "subject_saber_id_gen", sequenceName = "subject_saber_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_subject", nullable = false)
    private Subject idSubject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_saber", nullable = false)
    private Sabere idSaber;

}