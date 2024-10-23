package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Subject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "subject_schedule")
public class SubjectSchedule1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_schedule_id_gen")
    @SequenceGenerator(name = "subject_schedule_id_gen", sequenceName = "subject_schedule_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Size(max = 10)
    @NotNull
    @Column(name = "day_of_week", nullable = false, length = 10)
    private String dayOfWeek;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

}