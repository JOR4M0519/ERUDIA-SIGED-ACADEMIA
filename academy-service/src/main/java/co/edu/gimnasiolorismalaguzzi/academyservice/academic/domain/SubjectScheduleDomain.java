package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class SubjectScheduleDomain {
    private Integer id;
    private SubjectGroupDomain subjectGroup;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

}
