package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentTrackingDomain;

import java.util.List;

public interface PersistenceStudentTrackingPort extends PersistencePort<StudentTrackingDomain, Integer> {
    List<StudentTrackingDomain> getTrackingByStudentId(Integer id);
}
