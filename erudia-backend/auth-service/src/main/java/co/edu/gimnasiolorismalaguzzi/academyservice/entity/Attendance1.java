package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "attendance")
public class Attendance1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_id_gen")
    @SequenceGenerator(name = "attendance_id_gen", sequenceName = "attendance_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private SubjectSchedule schedule;

    @NotNull
    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Size(max = 2)
    @NotNull
    @Column(name = "status", nullable = false, length = 2)
    private String status;

    @ColumnDefault("now()")
    @Column(name = "recorded_at")
    private OffsetDateTime recordedAt;

}