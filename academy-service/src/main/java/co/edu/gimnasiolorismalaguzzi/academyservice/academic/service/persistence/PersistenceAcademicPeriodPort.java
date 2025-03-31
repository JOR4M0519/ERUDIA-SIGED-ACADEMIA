package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceAcademicPeriodPort extends PersistencePort<AcademicPeriodDomain, Integer> {
    List<AcademicPeriodDomain>  getAllPeriodsByStatus(String status);
    List<AcademicPeriodDomain> getPeriodsByYear(String year);

    List<AcademicPeriodDomain> getPeriodsBySettingsAndYear(Integer settingId, String year);

    List<AcademicPeriodDomain> getActivePeriodsByYear(String year);
}
