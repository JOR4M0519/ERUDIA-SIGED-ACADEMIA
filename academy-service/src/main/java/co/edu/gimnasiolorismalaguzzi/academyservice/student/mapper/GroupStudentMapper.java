package co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
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
