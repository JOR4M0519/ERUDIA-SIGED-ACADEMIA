package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.InstitutionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Institution;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "nit", target = "nit"),
            @Mapping(source = "address", target = "address"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "city", target = "city"),
            @Mapping(source = "department", target = "department"),
            @Mapping(source = "country", target = "country"),
            @Mapping(source = "postalCode", target = "postalCode"),
            @Mapping(source = "legalRepresentative", target = "legalRepresentative"),
            @Mapping(source = "incorporationDate", target = "incorporationDate"),
    })
    InstitutionDomain toDomain(Institution Institution);

    @InheritInverseConfiguration
    Institution toEntity(InstitutionDomain InstitutionDomain);

    List<InstitutionDomain> toDomains(List<Institution> Institutions);
    List<Institution> toEntities(List<InstitutionDomain> InstitutionsDomain);
}
