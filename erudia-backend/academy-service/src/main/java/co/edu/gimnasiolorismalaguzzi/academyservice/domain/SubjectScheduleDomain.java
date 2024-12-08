package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Subject;
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
