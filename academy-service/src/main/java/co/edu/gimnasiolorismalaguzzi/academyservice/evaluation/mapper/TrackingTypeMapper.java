package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.TrackingTypeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.TrackingType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackingTypeMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "type", target = "type"),
    })
    TrackingTypeDomain toDomain(TrackingType trackingType);

    @InheritInverseConfiguration
    TrackingType toEntity(TrackingTypeDomain trackingTypeDomain);

    List<TrackingTypeDomain> toDomains(List<TrackingType> trackingTypes);
    List<TrackingType> toEntities(List<TrackingType> trackingTypes);
}
