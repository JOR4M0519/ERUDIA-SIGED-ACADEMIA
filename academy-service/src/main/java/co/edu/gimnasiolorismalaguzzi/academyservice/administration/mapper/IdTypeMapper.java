package co.edu.gimnasiolorismalaguzzi.academyservice.administration.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.IdType;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.IdTypeDomain;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring")
public interface IdTypeMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name")
    })
    IdTypeDomain toDomain(IdType idType);

    @InheritInverseConfiguration
    IdType toEntity(IdTypeDomain idTypeDomain);

    List<IdTypeDomain> toDomains(List<IdType> idTypes);
    List<IdType> toEntities(List<IdTypeDomain> idTypeDomains);
}
