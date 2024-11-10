package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.ActivityGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Size(max = 256)
    @NotNull
    @Column(name = "email", nullable = false, length = 256)
    private String email;

    @Size(max = 256)
    @NotNull
    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @Column(name = "attempted_failed_login")
    private Integer attemptedFailedLogin;

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @OneToMany(mappedBy = "student")
    private Set<ActivityGrade> activityGrades = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Attendance> attendances = new LinkedHashSet<>();

    @OneToMany(mappedBy = "createdBy")
    private Set<BackupHistory> backupHistories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "professor")
    private Set<GroupStudent> groupStudents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<RecoveryPeriod> recoveryPeriods = new LinkedHashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<StudentTracking> studentTrackings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "professor")
    private Set<SubjectProfessor> subjectProfessors = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserDetail> userDetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles = new LinkedHashSet<>();

}
