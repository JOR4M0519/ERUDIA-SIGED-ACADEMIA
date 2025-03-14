package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectGroupDomain {
    private Integer id;
    private SubjectProfessorDomain subjectProfessor;
    private GroupsDomain groups;
    private AcademicPeriodDomain academicPeriod;
}
