package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class DimensionDomain {
    private Integer id;
    private String name;
    private String description;
}
