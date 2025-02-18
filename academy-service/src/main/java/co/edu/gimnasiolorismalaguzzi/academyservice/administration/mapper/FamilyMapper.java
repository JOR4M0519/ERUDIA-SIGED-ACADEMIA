package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Family;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FamilyMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "student", target = "relativeUser"),
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "relationship", target = "relationship")
    })
    FamilyDomain toDomain (Family family);

    @InheritInverseConfiguration
    Family toEntity(FamilyDomain familyDomain);

    List<FamilyDomain> toDomains (List<Family> familyList);
    List<Family> toEntities(List<FamilyDomain> familyDomainList);
}
