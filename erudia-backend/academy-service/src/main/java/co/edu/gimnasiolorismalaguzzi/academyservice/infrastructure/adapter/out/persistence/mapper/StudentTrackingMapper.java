package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.StudentTrackingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.StudentTracking;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentTrackingMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "professor", target = "professor"),
            @Mapping(source = "situation", target = "situation"),
            @Mapping(source = "compromise", target = "compromise"),
            @Mapping(source = "followUp", target = "followUp"),
            @Mapping(source = "status", target = "status")
    })
    StudentTrackingDomain toDomain(StudentTracking studentTracking);

    @InheritInverseConfiguration
    StudentTracking toEntity(StudentTrackingDomain studentTrackingDomain);

    List<StudentTrackingDomain> toDomains(List<StudentTracking> studentTrackingList);
    List<StudentTracking> toEntities (List<StudentTrackingDomain> studentTrackingDomainList);
}
