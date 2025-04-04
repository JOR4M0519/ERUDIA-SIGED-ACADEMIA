package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EducationalLevelDomain {
    private Integer id;
    private String levelName;
    private String status;
}
