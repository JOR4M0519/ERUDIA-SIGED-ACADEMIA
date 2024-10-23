package co.edu.gimnasiolorismalaguzzi.academyservice;

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
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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