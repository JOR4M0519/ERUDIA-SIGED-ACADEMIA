package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import lombok.Data;

@Data
public class GroupStudentDomain {
    private Integer id;
    private EducationalLevel level;
    private String groupCode;
    private String groupName;
    private User professor;
    private String status;
}
