package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.User;
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
@Table(name = "group_students")
public class GroupStudent1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_students_id_gen")
    @SequenceGenerator(name = "group_students_id_gen", sequenceName = "group_students_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "level_id", nullable = false)
    private EducationalLevel level;

    @Size(max = 15)
    @Column(name = "group_code", length = 15)
    private String groupCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @OneToMany(mappedBy = "group")
    private Set<ActivityGroup> activityGroups = new LinkedHashSet<>();

}