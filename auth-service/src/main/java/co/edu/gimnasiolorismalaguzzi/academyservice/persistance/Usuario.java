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
@Table(name = "usuario")
public class Usuario {
    @Id
    @ColumnDefault("nextval('usuario_id_usuario_seq')")
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @Size(max = 50)
    @NotNull
    @Column(name = "rol", nullable = false, length = 50)
    private String rol;

}