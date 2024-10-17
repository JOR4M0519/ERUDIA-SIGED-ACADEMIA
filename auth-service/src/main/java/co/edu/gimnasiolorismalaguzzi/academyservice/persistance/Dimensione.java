package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dimensiones")
public class Dimensione {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 60)
    @Column(name = "name", length = 60)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}