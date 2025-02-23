package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGroupDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGroup;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectGroupMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subjectProfessor", target = "subjectProfessor"),
            @Mapping(source = "groups", target = "groups")
    })
    SubjectGroupDomain toDomain(SubjectGroup subjectGroup);

    @InheritInverseConfiguration
    SubjectGroup toEntity(SubjectGroupDomain subjectGroupDomain);

    List<SubjectGroupDomain> toDomains(List<SubjectGroup> subjectGroups);
    List<SubjectGroup> toEntities(List<SubjectGroupDomain> subjectGroupDomains);
}
