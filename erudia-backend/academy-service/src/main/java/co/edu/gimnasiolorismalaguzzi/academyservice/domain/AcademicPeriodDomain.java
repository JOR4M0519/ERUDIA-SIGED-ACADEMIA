package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AcademicPeriodDomain {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String status;
}
