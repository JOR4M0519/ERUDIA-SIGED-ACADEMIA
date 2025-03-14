package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import lombok.*;


@Data
@Builder
public class GroupStudentsDomain {
    private Integer id;
    private UserDomain student;
    private GroupsDomain group;
}
