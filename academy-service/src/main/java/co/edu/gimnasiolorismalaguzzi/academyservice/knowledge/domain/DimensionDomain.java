package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DimensionDomain {
    private Integer id;
    private String name;
    private String description;
}
