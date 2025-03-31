package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.GradeSetting;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GradeSettingMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "levelId", target = "levelId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "minimumGrade", target = "minimumGrade"),
            @Mapping(source = "passGrade", target = "passGrade"),
            @Mapping(source = "maximumGrade", target = "maximumGrade")
    })
    GradeSettingDomain toDomain (GradeSetting gradeSetting);

    @InheritInverseConfiguration
    GradeSetting toEntity(GradeSettingDomain gradeSettingDomain);

    List<GradeSettingDomain> toDomains (List<GradeSetting> gradeSettings);
    List<GradeSetting> toEntities (List<GradeSettingDomain> gradeSettingDomains);
}
