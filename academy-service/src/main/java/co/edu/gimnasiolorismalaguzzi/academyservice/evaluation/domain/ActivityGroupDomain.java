package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityGroupDomain {
    private Integer id;
    private ActivityDomain activity;
    private GroupStudentsDomain group;
    private LocalDate startDate;
    private LocalDate endDate;
}

