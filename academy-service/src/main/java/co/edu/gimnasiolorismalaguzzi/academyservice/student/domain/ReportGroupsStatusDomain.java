package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import lombok.Data;

@Data
public class ReportGroupsStatusDomain {
    private Integer groupId;
    private String levelName;
    private String groupName;
    private String statusName;
    private Long studentsTotal;
}
