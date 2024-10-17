package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Sabere;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Subject;
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
@Table(name = "activity")
public class Activity1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_id_gen")
    @SequenceGenerator(name = "activity_id_gen", sequenceName = "activity_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "activity_name", nullable = false, length = 50)
    private String activityName;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject", nullable = false)
    private Subject subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", nullable = false)
    private AcademicPeriod period;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "saber", nullable = false)
    private Sabere saber;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @OneToMany(mappedBy = "activity")
    private Set<ActivityGroup> activityGroups = new LinkedHashSet<>();

}