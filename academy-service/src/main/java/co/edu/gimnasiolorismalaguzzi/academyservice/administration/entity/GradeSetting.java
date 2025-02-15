package co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "grade_settings")
public class GradeSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "level_id", nullable = false)
    private Integer levelId;

    @Column(name = "minimum_grade")
    private Integer minimumGrade;

    @Column(name = "pass_grade")
    private Integer passGrade;

    @Column(name = "maximum_grade")
    private Integer maximumGrade;

}