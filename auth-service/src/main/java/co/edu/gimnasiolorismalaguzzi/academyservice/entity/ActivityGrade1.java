package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "activity_grade")
public class ActivityGrade1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_grade_id_gen")
    @SequenceGenerator(name = "activity_grade_id_gen", sequenceName = "activity_grade_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private ActivityGroup activity;

    @NotNull
    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

}