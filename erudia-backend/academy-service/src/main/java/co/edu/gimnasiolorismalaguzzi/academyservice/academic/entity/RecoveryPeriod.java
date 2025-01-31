package co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "recovery_period")
public class RecoveryPeriod {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_grade", nullable = false)
    private SubjectGrade subjectGrade;

    @NotNull
    @Column(name = "previous_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal previousScore;

}