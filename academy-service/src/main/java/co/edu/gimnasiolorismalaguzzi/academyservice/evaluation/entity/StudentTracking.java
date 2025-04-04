package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "student_tracking")
public class StudentTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student", nullable = false)
    private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "professor", nullable = false)
    private User professor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "period_id", nullable = false)
    private AcademicPeriod period;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tracking_type", nullable = false)
    private TrackingType trackingType;

    @NotNull
    @Column(name = "situation", nullable = false, length = Integer.MAX_VALUE)
    private String situation;

    @NotNull
    @Column(name = "compromise", nullable = false, length = Integer.MAX_VALUE)
    private String compromise;

    @NotNull
    @Column(name = "follow_up", nullable = false, length = Integer.MAX_VALUE)
    private String followUp;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

}