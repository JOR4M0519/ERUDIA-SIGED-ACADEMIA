package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGrade;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityGradeMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "activity", target = "activity"),
            @Mapping(source = "score", target = "score"),
            @Mapping(source = "comment", target = "comment"),
    })

    ActivityGradeDomain toDomain(ActivityGrade activityGrade);

    @InheritInverseConfiguration
    ActivityGrade toEntity (ActivityGradeDomain activityGradeDomain);

    List<ActivityGradeDomain> toDomains(List<ActivityGrade> activityGrades);
    List<ActivityGrade> toEntities(List<ActivityGradeDomain> activityGradeDomains);
}
