package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.ActivityGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.GroupStudent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "activity_group")
public class ActivityGroup1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_group_id_gen")
    @SequenceGenerator(name = "activity_group_id_gen", sequenceName = "activity_group_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupStudent group;

    @NotNull
    @Column(name = "due", nullable = false)
    private LocalDate due;

    @OneToMany(mappedBy = "activity")
    private Set<ActivityGrade> activityGrades = new LinkedHashSet<>();

}