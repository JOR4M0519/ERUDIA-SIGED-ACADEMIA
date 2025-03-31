package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectGrade;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecoveryPeriodDomain {
    private Integer id;
    private SubjectGradeDomain subjectGrade;
    private BigDecimal previousScore;
    private GroupsDomain groupsDomain;
}
