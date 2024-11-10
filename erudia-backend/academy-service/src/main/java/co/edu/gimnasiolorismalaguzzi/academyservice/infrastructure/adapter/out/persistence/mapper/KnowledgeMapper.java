package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.KnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Knowledge;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KnowledgeMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "achievement", target = "achievement"),
            @Mapping(source = "status", target = "status")
    })
    KnowledgeDomain toDomain(Knowledge knowledge);

    @InheritInverseConfiguration
    Knowledge toEntity(KnowledgeDomain knowledgeDomain);

    List<KnowledgeDomain> toDomains(List<Knowledge> knowledgeList);
    List<Knowledge> toEntities (List<KnowledgeDomain> knowledgeDomains);
}
