package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;



import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "v_academic_report")
public class GradeReportView {
    @Id
    @Column(name = "grade_id")
    private Long gradeId;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "period_id")
    private Long periodId;

    @Column(name = "period_name")
    private String periodName;

    @Column(name = "academic_year")
    private String academicYear;

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

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "definitiva_score")
    private BigDecimal definitivaScore;

    @Column(name = "period_number")
    private Integer periodNumber;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "inasistencias")
    private Integer inasistencias;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", columnDefinition = "jsonb")
    private  List<Map<String, Object>> periodScores;
}

