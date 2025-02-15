package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecoveryPeriodDomain {
    private Integer id;
    private SubjectGrade subjectGrade;
    private BigDecimal previousScore;
}
