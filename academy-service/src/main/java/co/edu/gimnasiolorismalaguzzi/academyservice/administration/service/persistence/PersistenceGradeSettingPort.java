package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;

import java.util.List;

public interface PersistenceGradeSettingPort extends PersistencePort<GradeSettingDomain, Integer> {

    List<GradeSettingDomain> findByLevelId(Integer levelId);
}
