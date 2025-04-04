package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectDimension;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectDimensionMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subject", target = "subject"),
            @Mapping(source = "dimension", target = "dimension")
    })
    SubjectDimensionDomain toDomain(SubjectDimension subjectDimension);

    @InheritInverseConfiguration
    SubjectDimension toEntity(SubjectDimensionDomain subjectDimensionDomain);

    List<SubjectDimensionDomain> toDomains(List<SubjectDimension> subjectDimensions);
    List<SubjectDimensionDomain> toEntities(List<SubjectDimensionDomain> subjectDimensionDomains);
}
