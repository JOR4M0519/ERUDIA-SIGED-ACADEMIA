package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Knowledge;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Subject;
import lombok.Data;

@Data
public class SubjectKnowledgeDomain {
    private Integer id;
    private Subject idSubject;
    private Knowledge idKnowledge;
}
