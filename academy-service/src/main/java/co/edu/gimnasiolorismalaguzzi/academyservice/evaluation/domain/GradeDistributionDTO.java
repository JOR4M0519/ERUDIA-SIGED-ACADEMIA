package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeDistributionDTO {
    private String groupName;
    private Integer basicCount;
    private Integer highCount;
    private Integer superiorCount;
    private Integer totalStudents;
}
