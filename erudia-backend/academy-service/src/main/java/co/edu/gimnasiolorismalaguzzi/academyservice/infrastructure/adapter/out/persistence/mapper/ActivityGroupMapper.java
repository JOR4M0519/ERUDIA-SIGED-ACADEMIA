package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGroup;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityGroupMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "activity", target = "activityDomain"),
            @Mapping(source = "group", target = "group"),
            @Mapping(source = "due", target = "due")

    })
    ActivityGroupDomain toDomain(ActivityGroup activityGroup);

    @InheritInverseConfiguration
    ActivityGroup toEntity (ActivityGroupDomain activityGroupDomain);

    List<ActivityGroupDomain> toDomains(List<ActivityGroup> activityGroups);
    List<ActivityGroup> toEntities(List<ActivityGroupDomain> activityGroupDomains);
}
