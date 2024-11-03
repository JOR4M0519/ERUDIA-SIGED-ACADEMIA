package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.IdType;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.UserDetail;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.IdTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDetailDomain;
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
