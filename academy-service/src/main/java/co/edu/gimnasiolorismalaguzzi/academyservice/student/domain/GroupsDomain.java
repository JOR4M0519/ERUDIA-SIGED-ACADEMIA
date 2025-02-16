package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.EducationalLevel;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Data;

@Data
public class GroupsDomain {
    private Integer id;
    private EducationalLevelDomain level;
    private String groupCode;
    private String groupName;
    private UserDomain mentor;
    private String status;
}
