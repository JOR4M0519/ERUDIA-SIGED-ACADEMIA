package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.RecoveryPeriod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "academic_period")
public class AcademicPeriod1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "academic_period_id_gen")
    @SequenceGenerator(name = "academic_period_id_gen", sequenceName = "academic_period_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Size(max = 8)
    @NotNull
    @Column(name = "name", nullable = false, length = 8)
    private String name;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @OneToMany(mappedBy = "period")
    private Set<Activity> activities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "period")
    private Set<RecoveryPeriod> recoveryPeriods = new LinkedHashSet<>();

}