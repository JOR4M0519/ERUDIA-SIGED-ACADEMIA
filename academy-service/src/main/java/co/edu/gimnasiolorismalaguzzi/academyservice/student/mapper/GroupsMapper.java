package co.edu.gimnasiolorismalaguzzi.academyservice.student.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.Groups;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupsMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "level", target = "level"),
            @Mapping(source = "groupCode", target = "groupCode"),
            @Mapping(source = "groupName", target = "groupName"),
            @Mapping(source = "mentor", target = "mentor"),
            @Mapping(source = "status", target = "status")

    })
    GroupsDomain toDomain(Groups groups);

    @InheritInverseConfiguration
    Groups toEntity(GroupsDomain groupsDomain);

    List<GroupsDomain> toDomains(List<Groups> groups);
    List<Groups> toEntities(List<Groups> groups);
}
