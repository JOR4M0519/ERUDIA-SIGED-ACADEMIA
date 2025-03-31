package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.CreateActivityFront;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;

import java.util.List;

public interface PersistenceActivityPort extends PersistencePort<ActivityDomain, Integer> {

    ActivityDomain createActivityAndGroup(CreateActivityFront activityFront);
    

    List<ActivityDomain> getAllActivitiesWithKnowledgesAchievements(Integer id);

    ActivityDomain updateActivityAndGroup(Integer id, CreateActivityFront activityDomainFront);

    List<ActivityDomain> getAllActivitiesByAchievementGroupId(Integer id);
}
