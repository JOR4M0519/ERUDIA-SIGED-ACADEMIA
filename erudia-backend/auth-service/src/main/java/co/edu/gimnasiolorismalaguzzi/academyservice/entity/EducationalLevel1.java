package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.GroupStudent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "educational_level")
public class EducationalLevel1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "educational_level_id_gen")
    @SequenceGenerator(name = "educational_level_id_gen", sequenceName = "educational_level_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "level_name", nullable = false, length = 30)
    private String levelName;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @OneToMany(mappedBy = "level")
    private Set<GroupStudent> groupStudents = new LinkedHashSet<>();

}