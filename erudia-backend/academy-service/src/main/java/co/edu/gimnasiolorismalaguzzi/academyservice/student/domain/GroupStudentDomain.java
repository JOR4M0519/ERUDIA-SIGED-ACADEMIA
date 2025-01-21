package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
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
