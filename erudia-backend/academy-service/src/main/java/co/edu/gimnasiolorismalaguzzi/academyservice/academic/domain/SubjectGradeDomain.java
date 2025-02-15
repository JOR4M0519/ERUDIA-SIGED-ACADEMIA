package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.AcademicPeriod;
import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubjectGradeDomain {
    private Integer id;
    private Subject subject;
    private User student;
    private AcademicPeriod period;
    private BigDecimal totalScore;
    private String recovered;
}
