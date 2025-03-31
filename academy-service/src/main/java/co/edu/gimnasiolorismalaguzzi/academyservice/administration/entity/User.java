package co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
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

    @Size(max = 1)
    @NotNull
    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @Size(max = 80)
    @Column(name = "last_name", length = 80)
    private String lastName;

    @Size(max = 80)
    @Column(name = "first_name", length = 80)
    private String firstName;

    @Size(max = 256)
    @Column(name = "uuid", length = 256)
    private String uuid;
    @Size(max = 2)
    @NotNull
    @Column(name = "promotion_status", length = 2)
    private String promotionStatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /*@Id
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
    private String status;*/

/*    @OneToMany(mappedBy = "student")
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
    private Set<UserRole> userRoles = new LinkedHashSet<>();*/

}