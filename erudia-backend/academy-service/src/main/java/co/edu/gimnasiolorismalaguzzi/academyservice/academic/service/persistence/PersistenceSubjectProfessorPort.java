package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceSubjectProfessorPort extends PersistencePort<SubjectProfessorDomain, Integer> {
    List<SubjectProfessorDomain> findBySubjectId (Integer subjectId);
}
