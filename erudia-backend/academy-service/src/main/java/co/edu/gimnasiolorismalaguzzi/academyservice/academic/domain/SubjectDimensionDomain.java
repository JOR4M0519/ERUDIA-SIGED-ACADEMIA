package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import lombok.Data;

@Data
public class SubjectDimensionDomain {
    private Integer id;
    private DimensionDomain dimension;
    private SubjectDomain subject;
}
