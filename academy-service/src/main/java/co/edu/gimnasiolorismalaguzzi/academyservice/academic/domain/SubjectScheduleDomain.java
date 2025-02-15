package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import lombok.Data;

import java.time.LocalTime;

@Data
public class SubjectScheduleDomain {
    private Integer id;
    private Subject subject;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

}
