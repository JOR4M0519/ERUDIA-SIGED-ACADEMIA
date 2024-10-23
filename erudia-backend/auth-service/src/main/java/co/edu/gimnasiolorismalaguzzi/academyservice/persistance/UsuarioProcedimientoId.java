package co.edu.gimnasiolorismalaguzzi.academyservice.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UsuarioProcedimientoId implements Serializable {
    private static final long serialVersionUID = 5636356735635223004L;
    @NotNull
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @NotNull
    @Column(name = "id_procedimiento", nullable = false)
    private Integer idProcedimiento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsuarioProcedimientoId entity = (UsuarioProcedimientoId) o;
        return Objects.equals(this.idProcedimiento, entity.idProcedimiento) &&
                Objects.equals(this.idUsuario, entity.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProcedimiento, idUsuario);
    }

}