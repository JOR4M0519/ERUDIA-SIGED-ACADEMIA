package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain.DimensionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Dimension;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DimensionMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
    })
    DimensionDomain toDomain(Dimension dimension);

    @InheritInverseConfiguration
    Dimension toEntity(DimensionDomain dimensionDomain);

    List<DimensionDomain> toDomains(List<Dimension> dimensionList);
    List<Dimension> toEntity(List<DimensionDomain> dimensionDomainList);
}
