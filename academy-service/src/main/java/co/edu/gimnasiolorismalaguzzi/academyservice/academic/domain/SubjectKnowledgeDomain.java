package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import lombok.Data;

@Data
public class SubjectKnowledgeDomain {
    private Integer id;
    private Subject idSubject;
    private Knowledge idKnowledge;
}
