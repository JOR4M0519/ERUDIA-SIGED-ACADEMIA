package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectKnowledgeDomain {
    private Integer id;
    private Subject idSubject;
    private Knowledge idKnowledge;
}
