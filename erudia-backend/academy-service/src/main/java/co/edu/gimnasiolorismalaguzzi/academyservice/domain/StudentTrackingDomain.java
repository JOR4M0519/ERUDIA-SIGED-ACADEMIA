package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import lombok.Data;

@Data
public class StudentTrackingDomain {
    private Integer id;
    private User student;
    private User professor;
    private String situation;
    private String compromise;
    private String followUp;
    private String status;
}
