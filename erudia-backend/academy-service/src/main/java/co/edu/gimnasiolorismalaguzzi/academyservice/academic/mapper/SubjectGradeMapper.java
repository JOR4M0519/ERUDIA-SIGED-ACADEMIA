package co.edu.gimnasiolorismalaguzzi.academyservice.academic.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectGradeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectGradeMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "subject", target = "subject"),
            @Mapping(source = "student", target = "student"),
            @Mapping(source = "period", target = "period"),
            @Mapping(source = "totalScore", target = "totalScore"),
            @Mapping(source = "recovered", target = "recovered"),
    })
    SubjectGradeDomain toDomain(SubjectGrade subjectGrade);

    @InheritInverseConfiguration
    SubjectGrade toEntity(SubjectGradeDomain subjectGradeDomain);

    List<SubjectGradeDomain> toDomains(List<SubjectGrade> subjectGrades);

    List<SubjectGrade> toEntities(List<SubjectGradeDomain> subjectGradeDomains);
}
