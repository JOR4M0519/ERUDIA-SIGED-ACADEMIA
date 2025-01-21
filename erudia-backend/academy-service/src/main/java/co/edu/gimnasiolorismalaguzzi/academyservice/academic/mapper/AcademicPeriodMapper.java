package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AcademicPeriodMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "startDate", target = "startDate"),
            @Mapping(source = "endDate", target = "endDate"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "status", target = "status")
    })
    AcademicPeriodDomain toDomain(AcademicPeriod academicPeriod);

    @InheritInverseConfiguration
    AcademicPeriod toEntity(AcademicPeriodDomain academicPeriodDomain);

    List<AcademicPeriodDomain> toDomains(List<AcademicPeriod> academicPeriods);
    List<AcademicPeriod> toEntities(List<AcademicPeriodDomain> academicPeriodDomains);
}
