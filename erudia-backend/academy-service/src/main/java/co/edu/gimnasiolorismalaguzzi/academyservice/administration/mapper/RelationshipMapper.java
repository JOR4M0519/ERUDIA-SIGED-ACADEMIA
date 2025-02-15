package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.RelationshipDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.Relationship;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "relationshipType", target = "relationshipType")
    })
    RelationshipDomain toDomain(Relationship relationship);

    @InheritInverseConfiguration
    Relationship toEntity(RelationshipDomain relationshipDomain);

    List<RelationshipDomain> toDomains(List<Relationship> relationships);
    List<Relationship> toEntities (List<RelationshipDomain> relationshipDomains);
}
