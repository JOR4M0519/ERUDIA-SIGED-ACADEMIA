package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
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

    @ColumnDefault("now()")
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

}