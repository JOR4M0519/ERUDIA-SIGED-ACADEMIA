package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.RecoveryPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.RecoveryPeriod;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecoveryPeriodMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subjectGrade", target = "subjectGrade"),
            @Mapping(source = "previousScore", target = "previousScore")
    })
    RecoveryPeriodDomain toDomain(RecoveryPeriod recoveryPeriod);

    @InheritInverseConfiguration
    RecoveryPeriod toEntity(RecoveryPeriodDomain recoveryPeriodDomain);

    List<RecoveryPeriodDomain> toDomains(List<RecoveryPeriod> recoveryPeriods);
    List<RecoveryPeriod> toEntities(List<RecoveryPeriodDomain> recoveryPeriodDomains);
}
