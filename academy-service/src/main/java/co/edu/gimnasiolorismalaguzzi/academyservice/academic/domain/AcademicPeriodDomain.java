package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AcademicPeriodDomain {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String status;
}
