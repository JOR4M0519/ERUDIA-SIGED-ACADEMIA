package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectProfessorDomain {
    private Integer id;
    private Subject subject;
    private UserDomain professor;
}
