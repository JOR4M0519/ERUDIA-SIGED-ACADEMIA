package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.GradeDistributionDTO;

import java.util.List;

public interface PersistenceGradeReportService {
    List<GradeDistributionDTO> getGradeDistribution(Integer year, Integer periodId, String levelId, Integer subjectId);

}
