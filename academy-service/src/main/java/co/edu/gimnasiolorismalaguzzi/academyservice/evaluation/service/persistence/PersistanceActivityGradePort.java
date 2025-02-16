package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PersistanceActivityGradePort extends PersistencePort<ActivityGradeDomain, Integer> {

    List<ActivityGradeDomain> getAllActivity_ByPeriodUser(Integer periodId, Integer userId, String status);

    List<ActivityGradeDomain> getAllActivity_ByPeriod_Student_Subject(Integer subjectId,
                                                                      Integer periodId,
                                                                      Integer userId,
                                                                      String status);



}
