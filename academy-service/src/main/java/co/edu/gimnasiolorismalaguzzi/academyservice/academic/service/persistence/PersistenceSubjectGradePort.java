package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.math.BigDecimal;
import java.util.List;

public interface PersistenceSubjectGradePort extends PersistencePort<SubjectGradeDomain, Integer> {
    void recoverStudent(int idStudent, int idSubject, int idPeriod, BigDecimal newScore);

    List<SubjectGradeDomain>  findBySubjectPeriodStudentId(int groupId, int periodId, int studentId);
}
