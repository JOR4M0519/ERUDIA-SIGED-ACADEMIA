package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;

import java.util.List;

public interface PersistenceActivityPort extends PersistencePort<ActivityDomain, Integer> {
    List<ActivityDomain> getAllActivitiesWithKnowledgesAchievements(Integer id);
}
