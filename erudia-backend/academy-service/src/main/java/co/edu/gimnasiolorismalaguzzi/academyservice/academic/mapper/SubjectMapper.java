package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {SubjectProfessorMapper.class}
)
public interface SubjectMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subjectName", target = "subjectName"),
            @Mapping(target = "professor", ignore = true),
            @Mapping(source = "status", target = "status")
    })
    SubjectDomain toDomain(Subject subject);

    @InheritInverseConfiguration
    Subject toEntity(SubjectDomain subjectDomain);

    List<SubjectDomain> toDomains(List<Subject> subjects);
    List<User> toEntities(List<SubjectDomain> subjectsDomain);
}
