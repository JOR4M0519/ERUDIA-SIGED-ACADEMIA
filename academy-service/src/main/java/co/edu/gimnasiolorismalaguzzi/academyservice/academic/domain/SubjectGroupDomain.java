package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import lombok.Data;

@Data
public class SubjectGroupDomain {
    private Integer id;
    private SubjectProfessorDomain subjectProfessor;
    private GroupsDomain groups;
}
