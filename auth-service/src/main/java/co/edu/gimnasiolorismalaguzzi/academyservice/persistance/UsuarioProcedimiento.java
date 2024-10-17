package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario_procedimiento")
public class UsuarioProcedimiento {
    @EmbeddedId
    private UsuarioProcedimientoId id;

    @MapsId("idUsuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @MapsId("idProcedimiento")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_procedimiento", nullable = false)
    private Procedimiento idProcedimiento;

}