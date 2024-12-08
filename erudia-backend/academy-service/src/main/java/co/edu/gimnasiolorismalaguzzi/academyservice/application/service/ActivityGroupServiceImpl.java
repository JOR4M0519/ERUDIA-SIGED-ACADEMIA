package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.ActivityGroupServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceActivityGroupPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGroupDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class ActivityGroupServiceImpl implements ActivityGroupServicePort {

    private final PersistenceActivityGroupPort reporsitory;

    @Autowired
    public ActivityGroupServiceImpl(PersistenceActivityGroupPort reporsitory) {
        this.reporsitory = reporsitory;
    }

    @Override
    public List<ActivityGroupDomain> getAllActivity_Group() {
        return reporsitory.findAll();
    }

    @Override
    public ActivityGroupDomain getActivityGroupById(Integer id) {
        return reporsitory.findById(id);
    }

    @Override
    public ActivityGroupDomain createActivityGroup(ActivityGroupDomain activityGroupDomain) {
        return reporsitory.save(activityGroupDomain);
    }

    @Override
    public ActivityGroupDomain updateActivityGroup(Integer id, ActivityGroupDomain activityGroupDomain) {
        return reporsitory.update(id,activityGroupDomain);
    }

    @Override
    public void deleteActivityGroup(Integer id) {
        reporsitory.delete(id);
    }
}
