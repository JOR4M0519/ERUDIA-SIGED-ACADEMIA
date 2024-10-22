package co.edu.gimnasiolorismalaguzzi.academyservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student", nullable = false)
    private User student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor", nullable = false)
    private User professor;

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
    @Column(name = "status", nullable = false)
    private Boolean status = false;

}