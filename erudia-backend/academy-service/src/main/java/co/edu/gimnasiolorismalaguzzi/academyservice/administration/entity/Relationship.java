package co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "relationship")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @Column(name = "relationship_type", length = 10)
    private String relationshipType;

}