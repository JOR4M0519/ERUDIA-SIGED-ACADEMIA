package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.Permission;
import co.edu.gimnasiolorismalaguzzi.authservice.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role_perm")
public class RolePerm1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_perm_id_gen")
    @SequenceGenerator(name = "role_perm_id_gen", sequenceName = "role_perm_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

}