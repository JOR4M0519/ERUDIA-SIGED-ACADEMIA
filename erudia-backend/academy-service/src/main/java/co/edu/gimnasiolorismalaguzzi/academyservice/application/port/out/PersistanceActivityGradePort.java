package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGrade;

public interface PersistanceActivityGradePort extends PersistencePort<ActivityGradeDomain, Integer>{
}
