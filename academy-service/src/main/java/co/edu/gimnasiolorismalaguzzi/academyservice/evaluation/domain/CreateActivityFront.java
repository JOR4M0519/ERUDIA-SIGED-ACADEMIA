package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.AchievementGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateActivityFront {
    private Integer id;
    private GroupStudentsDomain group;
    private LocalDate startDate;
    private LocalDate endDate;
    private String activityName;
    private String description;
    private AchievementGroupDomain achievementGroup;
    private String status;
}
