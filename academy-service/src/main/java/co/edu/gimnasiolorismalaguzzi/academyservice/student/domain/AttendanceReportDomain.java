package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceReportDomain {
    private Integer groupId;
    private String levelName;
    private String groupName;
    private String sectionName;
    private Long totalActive;
    private Long presentCount;
    private Long absentCount;
    private Long lateCount;
    private LocalDateTime lastRecord;
}