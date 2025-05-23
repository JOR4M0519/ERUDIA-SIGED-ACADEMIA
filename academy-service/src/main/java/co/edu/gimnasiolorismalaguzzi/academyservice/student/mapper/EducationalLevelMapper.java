package co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.EducationalLevelDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EducationalLevelMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "levelName", target = "levelName"),
            @Mapping(source = "status", target = "status")
    })
    EducationalLevelDomain toDomain(EducationalLevel EducationalLevel);

    @InheritInverseConfiguration
    EducationalLevel toEntity(EducationalLevelDomain EducationalLevelDomain);

    List<EducationalLevelDomain> toDomains(List<EducationalLevel> EducationalLevels);
    List<EducationalLevel> toEntities(List<EducationalLevelDomain> EducationalLevelsDomain);
}
