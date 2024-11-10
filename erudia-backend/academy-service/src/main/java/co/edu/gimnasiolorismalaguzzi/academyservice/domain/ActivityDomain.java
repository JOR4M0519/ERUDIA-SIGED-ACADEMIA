package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Subject;
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
