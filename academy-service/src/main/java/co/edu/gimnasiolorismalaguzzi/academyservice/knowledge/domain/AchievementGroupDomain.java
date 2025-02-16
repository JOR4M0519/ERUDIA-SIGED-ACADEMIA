package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import lombok.Data;

@Data
public class AchievementGroupDomain {
    private Integer id;
    private SubjectKnowledgeDomain subjectKnowledgeDomain;
    private String achievement;
    private GroupsDomain group;
    private AcademicPeriodDomain period;
}
