package co.edu.gimnasiolorismalaguzzi.academyservice.academic.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;

import java.util.List;

public interface PersistenceAcademicPeriodPort extends PersistencePort<AcademicPeriodDomain, Integer> {
    List<AcademicPeriodDomain>  getAllPeriodsByStatus(String status);
}
