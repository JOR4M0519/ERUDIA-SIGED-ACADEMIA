package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.ActivityGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGrade;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityGradeMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "score", target = "score"),
            @Mapping(source = "comment", target = "comment"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "activity", target = "activity", ignore = true)
    })
    ActivityGradeDomain toDomain(ActivityGrade activityGrade);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "score", target = "score"),
            @Mapping(source = "comment", target = "comment"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "activity", target = "activity")
    })
    ActivityGrade toEntity(ActivityGradeDomain activityGradeDomain);

    List<ActivityGradeDomain> toDomains(List<ActivityGrade> activityGrades);
    List<ActivityGrade> toEntities(List<ActivityGradeDomain> activityGradeDomains);
}
