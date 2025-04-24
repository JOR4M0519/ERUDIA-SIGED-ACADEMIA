package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicYearPercentageDTO {
    private Integer year;
    private Integer totalPercentage;
    private Boolean isComplete; // Indica si el porcentaje suma 100%
}