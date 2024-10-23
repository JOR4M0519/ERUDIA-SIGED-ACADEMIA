package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Dimensione;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subject_dimention")
public class SubjectDimention1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_dimention_id_gen")
    @SequenceGenerator(name = "subject_dimention_id_gen", sequenceName = "subject_dimention_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dimention_id", nullable = false)
    private Dimensione dimention;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

}