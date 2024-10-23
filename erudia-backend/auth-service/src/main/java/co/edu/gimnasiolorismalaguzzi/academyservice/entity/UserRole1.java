package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.User;
import co.edu.gimnasiolorismalaguzzi.authservice.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_role")
public class UserRole1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_id_gen")
    @SequenceGenerator(name = "user_role_id_gen", sequenceName = "user_role_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}