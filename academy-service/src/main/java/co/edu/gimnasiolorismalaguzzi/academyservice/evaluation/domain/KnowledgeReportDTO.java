package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KnowledgeReportDTO {
    private Long knowledgeId;
    private String knowledgeName;
    private Integer percentage;
    private String achievement;
    private BigDecimal score;
    private BigDecimal definitivaScore;
}
