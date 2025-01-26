package co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "role_name", nullable = false, length = 20)
    private String roleName;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @OneToMany(mappedBy = "role")
    private Set<RolePerm> rolePerms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new LinkedHashSet<>();

}