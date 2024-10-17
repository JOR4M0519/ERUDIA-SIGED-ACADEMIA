package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.RolePerm;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_id_gen")
    @SequenceGenerator(name = "permission_id_gen", sequenceName = "permission_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "permission", nullable = false, length = Integer.MAX_VALUE)
    private String permission;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status = false;

    @OneToMany(mappedBy = "permission")
    private Set<RolePerm> rolePerms = new LinkedHashSet<>();

}