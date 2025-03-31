package co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academic_period")
public class AcademicPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "percentage")
    private Integer percentage;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "setting_id", nullable = false)
    private GradeSetting setting;

//    @OneToMany(mappedBy = "period")
//    private Set<Activity> activities = new LinkedHashSet<>();
//
//    @OneToMany(mappedBy = "period")
//    private Set<RecoveryPeriod> recoveryPeriods = new LinkedHashSet<>();

}