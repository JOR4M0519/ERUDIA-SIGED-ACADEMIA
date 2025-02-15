package co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupStudentsMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "group", target = "group")
    })
    GroupStudentsDomain toDomain(GroupStudent groupStudent);
    @InheritInverseConfiguration
    GroupStudent toEntity(GroupStudentsDomain groupStudentsDomain);

    List<GroupStudentsDomain> toDomains(List<GroupStudent> groupStudents);
    List<GroupStudent> toEntities(List<GroupStudentsDomain> groupStudentsDomains);
}
