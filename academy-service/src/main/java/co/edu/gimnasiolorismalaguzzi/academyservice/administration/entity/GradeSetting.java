package co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Size(max = 40)
    @NotNull
    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Size(max = 400)
    @NotNull
    @Column(name = "description", length = 400)
    private String description;

    @Column(name = "minimum_grade")
    private Integer minimumGrade;

    @Column(name = "pass_grade")
    private Integer passGrade;

    @Column(name = "maximum_grade")
    private Integer maximumGrade;

}