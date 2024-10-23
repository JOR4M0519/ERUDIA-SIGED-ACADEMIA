package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "educational_level")
public class EducationalLevel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "level_name", nullable = false, length = 30)
    private String levelName;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

}