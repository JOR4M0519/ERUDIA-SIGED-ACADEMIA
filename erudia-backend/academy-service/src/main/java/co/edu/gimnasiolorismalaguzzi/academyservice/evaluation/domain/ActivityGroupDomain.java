package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityGroupDomain {
    private Integer id;
    private Activity activityDomain;
    private GroupStudent group;
    private LocalDate due;
}

