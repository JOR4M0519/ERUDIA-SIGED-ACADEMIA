package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.Activity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityMapper {



    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "activityName", target = "activityName"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "achievementGroup", target = "achievementGroup"),
            @Mapping(source = "status", target = "status"),
    })

    ActivityDomain toDomain(Activity activity);



    @InheritInverseConfiguration
    Activity toEntity (ActivityDomain activityDomain);

    List<ActivityDomain> toDomains(List<Activity> activityList);
    List<Activity> toEntities(List<Activity> activityList);
}

