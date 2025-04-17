package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SubjectReportDTO {
    private Long subjectId;
    private String subjectName;
    private BigDecimal totalScore;
    private Boolean recovered;
    private String comment;
    private String teacherName;
    private String area;
    private String desempenio;
    private List<KnowledgeReportDTO> knowledges = new ArrayList<>();
    private List<Map<String, Object>> periodScores;
}