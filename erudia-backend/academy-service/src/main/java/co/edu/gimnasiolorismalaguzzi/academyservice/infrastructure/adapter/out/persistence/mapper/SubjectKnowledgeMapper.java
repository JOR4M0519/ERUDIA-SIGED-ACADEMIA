package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.mapper;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.StudentTrackingDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.StudentTracking;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.SubjectKnowledge;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectKnowledgeMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "idSubject", target = "idSubject"),
            @Mapping(source = "idKnowledge", target = "idKnowledge")
    })
    SubjectKnowledgeDomain toDomain(SubjectKnowledge subjectKnowledge);

    @InheritInverseConfiguration
    SubjectKnowledge toEntity(SubjectKnowledgeDomain subjectKnowledgeDomain);

    List<SubjectKnowledgeDomain> toDomains(List<SubjectKnowledge> subjectKnowledges);
    List<SubjectKnowledge> toEntities (List<SubjectKnowledgeDomain> subjectKnowledgeDomains);

}
