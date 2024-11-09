package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.ActivityGradeServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistanceActivityGradePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceActivityGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGradeDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class ActivityGradeServiceImpl implements ActivityGradeServicePort {

    private final PersistanceActivityGradePort port;

    @Autowired
    public ActivityGradeServiceImpl(PersistanceActivityGradePort port) {
        this.port = port;
    }

    @Override
    public List<ActivityGradeDomain> getAttAcitivitiesWithGrades() {
        return port.findAll();
    }

    @Override
    public ActivityGradeDomain getActivityGradeById(Integer id) {
        return port.findById(id);
    }

    @Override
    public ActivityGradeDomain createActivityGrade(ActivityGradeDomain activityGradeDomain) {
        return port.save(activityGradeDomain);
    }

    @Override
    public ActivityGradeDomain updateActivityGrade(Integer id, ActivityGradeDomain activityGradeDomain) {
        return port.update(id, activityGradeDomain);
    }
}
