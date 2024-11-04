package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AcademicPeriodDomain;

import java.util.List;

public interface AcademicPeriodServicePort {
    List<AcademicPeriodDomain> getAllPeriods();
    AcademicPeriodDomain getPeriodById(Integer id);
    AcademicPeriodDomain createPeriod(AcademicPeriodDomain academicPeriodDomain);
    AcademicPeriodDomain updatePeriod(Integer id, AcademicPeriodDomain academicPeriodDomain);
    void deletePeriod(Integer id);
}
