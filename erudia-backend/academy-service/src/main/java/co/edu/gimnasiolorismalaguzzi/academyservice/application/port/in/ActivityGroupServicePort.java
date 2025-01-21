package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGroupDomain;

import java.util.List;

public interface ActivityGroupServicePort {
    List<ActivityGroupDomain> getAllActivity_Group();
    ActivityGroupDomain getActivityGroupById(Integer id);
    ActivityGroupDomain createActivityGroup(ActivityGroupDomain activityGroupDomain);
    ActivityGroupDomain updateActivityGroup(Integer id, ActivityGroupDomain activityGroupDomain);
    void deleteActivityGroup(Integer id);

}
