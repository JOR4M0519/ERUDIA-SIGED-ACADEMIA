package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;

import java.util.List;
import java.util.Optional;

public interface PersistenceActivityGroupPort extends PersistencePort<ActivityGroupDomain, Integer> {
    List<ActivityGroupDomain> findActivitiesByGroupId(Integer id, String status);

    List<ActivityGroupDomain> getAllActivity_ByPeriodUser(Integer periodId, Integer userId, String i);

    List<ActivityGroupDomain> getAllActivityBySubjectAndPeriodAndGroupIdAndStatusNotLike(Integer subjectId, Integer periodId, Integer groupId, String statusNotLike);

    List<ActivityGroupDomain> getAllActivity_ByPeriodSubjectGroup(Integer subjectId, Integer periodId, Integer groupId, String statusNotLike);

    ActivityGroupDomain getRangeDateActivityByActivityId(Integer activityId);

    Optional<ActivityGroup> findByActivity_IdAndGroup_Id(Integer activityId, Integer groupId);
}
