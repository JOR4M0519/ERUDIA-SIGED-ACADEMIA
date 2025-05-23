package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActivityDomain {
    private Integer id;
    private String activityName;
    private String description;
    private AchievementGroupDomain achievementGroup;
    private String status;
}
