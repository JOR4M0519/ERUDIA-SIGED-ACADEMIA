package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectProfessorDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectProfessor;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectProfessorMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subject", target = "subject"),
            @Mapping(source = "professor", target = "professor")
    })
    SubjectProfessorDomain toDomain (SubjectProfessor subjectProfessor);

    @InheritInverseConfiguration
    SubjectProfessor toEntity (SubjectProfessorDomain subjectProfessorDomain);

    List<SubjectProfessorDomain> toDomains (List<SubjectProfessor> subjectProfessorList);
    List<SubjectProfessor> toEntities(List<SubjectProfessorDomain> subjectProfessorDomains);
}
