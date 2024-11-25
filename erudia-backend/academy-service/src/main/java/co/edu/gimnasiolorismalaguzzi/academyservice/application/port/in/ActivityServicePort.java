package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityDomain;

import java.util.List;

public interface ActivityServicePort {
    List<ActivityDomain> getAllActivities();
    ActivityDomain getActivityById(Integer id);
    ActivityDomain createActivity(ActivityDomain activityDomain);
    ActivityDomain updateActivity(Integer id, ActivityDomain activity);
    void deleteActivity(Integer id);
}
