package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.math.BigDecimal;

public interface PersistenceSubjectGradePort extends PersistencePort<SubjectGradeDomain, Integer> {
    void recoverStudent(int idStudent, int idSubject, int idPeriod, BigDecimal newScore);
}
