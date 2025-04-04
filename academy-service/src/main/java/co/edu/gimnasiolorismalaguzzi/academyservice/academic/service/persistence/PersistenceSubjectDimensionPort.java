package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;


public interface PersistenceSubjectDimensionPort extends PersistencePort<SubjectDimensionDomain, Integer> {
    List<SubjectDimensionDomain> getAllByDimensionId(Integer dimensionId);
}
