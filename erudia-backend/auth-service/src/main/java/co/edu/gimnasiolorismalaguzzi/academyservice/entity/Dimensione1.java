package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import co.edu.gimnasiolorismalaguzzi.academyservice.persistance.SubjectDimention;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dimensiones")
public class Dimensione1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dimensiones_id_gen")
    @SequenceGenerator(name = "dimensiones_id_gen", sequenceName = "dimensiones_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 60)
    @Column(name = "name", length = 60)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @OneToMany(mappedBy = "dimention")
    private Set<SubjectDimention> subjectDimentions = new LinkedHashSet<>();

}