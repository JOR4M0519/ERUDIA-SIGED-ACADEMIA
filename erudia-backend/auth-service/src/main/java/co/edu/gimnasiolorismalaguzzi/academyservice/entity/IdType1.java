package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.UserDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "id_type")
public class IdType1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_type_id_gen")
    @SequenceGenerator(name = "id_type_id_gen", sequenceName = "id_type_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @OneToMany(mappedBy = "idType")
    private Set<UserDetail> userDetails = new LinkedHashSet<>();

}