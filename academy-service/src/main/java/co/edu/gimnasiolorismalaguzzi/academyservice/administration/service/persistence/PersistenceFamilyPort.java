package co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserFamilyRelationDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceFamilyPort extends PersistencePort<FamilyDomain, Integer> {
    List<UserFamilyRelationDomain> findAllWithRelatives();

    List<FamilyDomain> findAllByRelationType(Integer relationTypeId);

    List<FamilyDomain> saveFamilyRelations(UserFamilyRelationDomain userFamilyRelationDomain);

    boolean existsRelation(Integer studentId, Integer relativeId);

    List<FamilyDomain> findRelativesByStudent(Integer id);

    FamilyDomain saveById(Integer id, FamilyDomain familyDomain);


    List<FamilyDomain> findStudentsByRelativeId(Integer relativeId);

    List<FamilyReportDomain> getAllFamilyReports();
}
