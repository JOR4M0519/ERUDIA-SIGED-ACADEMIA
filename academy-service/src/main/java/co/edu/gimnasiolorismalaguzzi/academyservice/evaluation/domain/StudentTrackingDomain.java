package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain.AcademicPeriodDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentTrackingDomain {
    private Integer id;
    private UserDomain student;
    private UserDomain professor;
    private AcademicPeriodDomain period;
    private TrackingTypeDomain trackingType;
    private String situation;
    private String compromise;
    private LocalDate date;
    private String followUp;
    private String status;
}
