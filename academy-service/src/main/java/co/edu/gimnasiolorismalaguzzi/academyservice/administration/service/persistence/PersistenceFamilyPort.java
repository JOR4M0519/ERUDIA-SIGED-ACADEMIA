package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceFamilyPort extends PersistencePort<FamilyDomain, Integer> {
    List<FamilyDomain> findRelativesByStudent(Integer id);

    FamilyDomain saveById(Integer id, FamilyDomain familyDomain);

    List<FamilyDomain> findStudentsByRelativeId(Integer relativeId);

}
