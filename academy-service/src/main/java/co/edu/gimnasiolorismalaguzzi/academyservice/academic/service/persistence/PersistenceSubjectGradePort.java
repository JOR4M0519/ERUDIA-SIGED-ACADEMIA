package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.RecoveryPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.math.BigDecimal;
import java.util.List;

public interface PersistenceSubjectGradePort extends PersistencePort<SubjectGradeDomain, Integer> {
    List<RecoveryPeriodDomain> findRecoveryListSubjects(int subjectId, String year,int levelId);

    void recoverStudent(int idStudent, int idSubject, int idPeriod, BigDecimal newScore);

    SubjectGradeDomain saveOrUpdateSubjectGrade(Integer studentId, Integer subjectId, Integer periodId, BigDecimal finalScore);

    List<SubjectGradeDomain>  findBySubjectPeriodStudentId(int groupId, int periodId, int studentId);

    void editRecoverStudent(int recoveryId, BigDecimal newScore,String status);

    void deleteRecoverStudent(int recoverId);
}
