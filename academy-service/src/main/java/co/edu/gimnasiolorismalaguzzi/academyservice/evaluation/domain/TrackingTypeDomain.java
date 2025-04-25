package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackingTypeDomain {
    private Integer id;
    private String type;
}
