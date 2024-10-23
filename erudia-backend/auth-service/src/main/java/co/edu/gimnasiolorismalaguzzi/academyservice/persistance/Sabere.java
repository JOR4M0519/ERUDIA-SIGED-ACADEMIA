package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "saberes")
public class Sabere {
    @Id
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