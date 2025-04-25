package co.edu.gimnasiolorismalaguzzi.academyservice.knowledge.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.SubjectKnowledgeDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementGroupDomain {
    private Integer id;
    private SubjectKnowledgeDomain subjectKnowledge;
    private String achievement;
    private GroupsDomain group;
    private AcademicPeriodDomain period;
}
