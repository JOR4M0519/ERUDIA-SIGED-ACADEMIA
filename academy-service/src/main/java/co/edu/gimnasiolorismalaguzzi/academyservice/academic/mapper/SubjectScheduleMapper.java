package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectScheduleDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectScheduleMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "subjectGroup", target = "subjectGroup"),
        @Mapping(source = "dayOfWeek", target = "dayOfWeek"),
        @Mapping(source = "startTime", target = "startTime"),
        @Mapping(source = "endTime", target = "endTime"),
        @Mapping(source = "status", target = "status")
    })
    SubjectScheduleDomain toDomain(SubjectSchedule subjectSchedule);

    @InheritInverseConfiguration
    SubjectSchedule toEntity(SubjectScheduleDomain subjectScheduleDomain);

    List<SubjectScheduleDomain> toDomains(List<SubjectSchedule> subjectScheduleList);
    List<SubjectSchedule>toEntities(List<SubjectScheduleDomain> subjectScheduleDomainList);
}
