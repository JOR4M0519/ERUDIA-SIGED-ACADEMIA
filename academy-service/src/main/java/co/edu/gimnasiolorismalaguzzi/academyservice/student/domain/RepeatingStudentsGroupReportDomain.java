package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepeatingStudentsGroupReportDomain {
    private Integer groupId;
    private String groupName;
    private String levelName;
    private Long repeatingCount;
}
