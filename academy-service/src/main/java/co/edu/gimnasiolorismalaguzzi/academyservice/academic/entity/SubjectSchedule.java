package co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject_schedule")
public class SubjectSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_group_id", nullable = false)
    private SubjectGroup subjectGroup;

    @Size(max = 10)
    @Column(name = "day_of_week", length = 10)
    private String dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

//    @OneToMany(mappedBy = "schedule")
//    private Set<Attendance> attendances = new LinkedHashSet<>();

}