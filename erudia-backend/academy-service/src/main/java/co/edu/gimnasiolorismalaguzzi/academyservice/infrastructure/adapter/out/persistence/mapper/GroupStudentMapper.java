package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.GroupStudentDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.InstitutionDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.GroupStudent;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Institution;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupStudentMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "level", target = "level"),
            @Mapping(source = "groupCode", target = "groupCode"),
            @Mapping(source = "groupName", target = "groupName"),
            @Mapping(source = "professor", target = "professor"),
            @Mapping(source = "status", target = "status")

    })
    GroupStudentDomain toDomain(GroupStudent groupStudent);

    @InheritInverseConfiguration
    GroupStudent toEntity(GroupStudentDomain groupStudentDomain);

    List<GroupStudentDomain> toDomains(List<GroupStudent> groupStudents);
    List<GroupStudent> toEntities(List<GroupStudent> groupStudents);
}
