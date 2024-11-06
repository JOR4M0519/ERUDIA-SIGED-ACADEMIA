package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.ActivityServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceActivityPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class ActivityServiceImpl implements ActivityServicePort {

    private final PersistenceActivityPort activityRepository;

    @Autowired
    public ActivityServiceImpl(PersistenceActivityPort activityRepository) {
        this.activityRepository = activityRepository;
    }


    @Override
    public List<ActivityDomain> getAllActivities() {
        return activityRepository.findAll();
    }

    @Override
    public ActivityDomain getActivityById(Integer id) {
        return activityRepository.findById(id);
    }

    @Override
    public ActivityDomain createActivity(ActivityDomain activity) {
        return activityRepository.save(activity);
    }

    @Override
    public ActivityDomain updateActivity(Integer id, ActivityDomain activity) {
        return activityRepository.update(id,activity);
    }


    @Override
    public void deleteActivity(Integer id) {
        activityRepository.delete(id);
    }
}
