package co.edu.gimnasiolorismalaguzzi.academyservice.adapter.out.persistence.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "role_name", nullable = false, length = 20)
    private String roleName;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

}