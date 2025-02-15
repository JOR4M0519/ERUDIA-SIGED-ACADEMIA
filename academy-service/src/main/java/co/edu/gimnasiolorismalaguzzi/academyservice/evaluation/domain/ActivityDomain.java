package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import lombok.Data;

@Data
public class ActivityDomain {
    private Integer id;
    private String activityName;
    private String description;
    private Subject subject;
    private AcademicPeriod period;
    private Knowledge knowledge;
    private String status;
}
