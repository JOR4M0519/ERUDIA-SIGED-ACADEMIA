package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PersistanceActivityGradePort extends PersistencePort<ActivityGradeDomain, Integer> {
    ActivityGradeDomain getGradeByActivityId(Integer id);
}
