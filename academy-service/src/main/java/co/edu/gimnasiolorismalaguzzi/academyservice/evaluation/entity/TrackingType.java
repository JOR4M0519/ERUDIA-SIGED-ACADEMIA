package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tracking_type")
public class TrackingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 1)
    @Column(name = "type", length = 1)
    private String type;

}