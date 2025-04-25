package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StudentPromotionDTO {
    private List<Integer> studentIds;  // IDs de los estudiantes a promover
    private Integer targetGroupId;     // ID del grupo destino
    private String promotionStatus;    // Estado de promoción

    public StudentPromotionDTO(List<Integer> integers, int i) {
    }
}
