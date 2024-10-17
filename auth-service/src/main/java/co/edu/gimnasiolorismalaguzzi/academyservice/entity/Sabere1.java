package co.edu.gimnasiolorismalaguzzi.academyservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "saberes")
public class Sabere1 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saberes_id_gen")
    @SequenceGenerator(name = "saberes_id_gen", sequenceName = "saberes_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @ColumnDefault("NULL")
    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "archivement", length = Integer.MAX_VALUE)
    private String archivement;

    @Column(name = "status")
    private Boolean status;

}