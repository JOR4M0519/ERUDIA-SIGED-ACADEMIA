package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeReport {
    @Id
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "period_id")
    private Long periodId;

    @Column(name = "period_name")
    private String periodName;

    @Column(name = "total_score")
    private BigDecimal totalScore;

    @Column(name = "recovered")
    private String recovered;

    @Column(name = "comment")
    private String comment;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "subject_knowledge_id")
    private Long subjectKnowledgeId;

    @Column(name = "knowledge_id")
    private Long knowledgeId;

    @Column(name = "knowledge_name")
    private String knowledgeName;

    @Column(name = "knowledge_percentage")
    private Integer knowledgePercentage;

    @Column(name = "achievement_group_id")
    private Long achievementGroupId;

    @Column(name = "achievement")
    private String achievement;
}
