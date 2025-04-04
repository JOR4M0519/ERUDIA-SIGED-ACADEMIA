package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;

import java.util.List;

public interface PersistanceActivityGradePort extends PersistencePort<ActivityGradeDomain, Integer> {
    List<ActivityGradeDomain> getGradeByActivityIdGroupId(Integer id, Integer groupId);

    ActivityGradeDomain getGradeByActivityGroupIdByStudentId(Integer activityId, Integer studentId);

    ActivityGradeDomain getGradeByActivityIdByStudentId(Integer activityId, Integer studentId);
}
