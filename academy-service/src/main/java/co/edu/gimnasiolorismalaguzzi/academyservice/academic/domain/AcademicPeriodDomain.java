package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.GradeSettingDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class AcademicPeriodDomain {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String status;
    private Integer percentage;
    private GradeSettingDomain gradeSetting;
}
