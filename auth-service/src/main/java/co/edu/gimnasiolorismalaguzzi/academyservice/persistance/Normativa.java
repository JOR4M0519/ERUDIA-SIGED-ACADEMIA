package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "normativa")
public class Normativa {
    @Id
    @ColumnDefault("nextval('normativa_id_normativa_seq')")
    @Column(name = "id_normativa", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "titulo_normativa", nullable = false, length = 100)
    private String tituloNormativa;

}