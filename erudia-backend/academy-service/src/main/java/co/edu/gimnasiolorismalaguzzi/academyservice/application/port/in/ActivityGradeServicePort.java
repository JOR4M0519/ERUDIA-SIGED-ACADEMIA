package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGrade;

import java.util.List;

public interface ActivityGradeServicePort {
    List<ActivityGradeDomain> getAttAcitivitiesWithGrades();
    ActivityGradeDomain getActivityGradeById(Integer id);
    ActivityGradeDomain createActivityGrade(ActivityGradeDomain activityGradeDomain);
    ActivityGradeDomain updateActivityGrade(Integer id, ActivityGradeDomain activityGradeDomain);
}
