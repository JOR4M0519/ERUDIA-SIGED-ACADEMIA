package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;


import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subjectName", target = "subjectName"),
            @Mapping(source = "status", target = "status")
    })
    SubjectDomain toDomain(Subject subject);

    @InheritInverseConfiguration
    Subject toEntity(SubjectDomain subjectDomain);

    List<SubjectDomain> toDomains(List<Subject> subjects);
    List<User> toEntities(List<SubjectDomain> subjectsDomain);
}
