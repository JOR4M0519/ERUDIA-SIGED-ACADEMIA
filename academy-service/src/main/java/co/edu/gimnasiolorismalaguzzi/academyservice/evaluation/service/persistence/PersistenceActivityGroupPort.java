package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;

import java.util.List;

public interface PersistenceActivityGroupPort extends PersistencePort<ActivityGroupDomain, Integer> {
    List<ActivityGroupDomain> findActivitiesByGroupId(Integer id, String status);
}
