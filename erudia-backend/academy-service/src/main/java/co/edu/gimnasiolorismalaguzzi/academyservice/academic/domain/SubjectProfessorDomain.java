package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.Subject;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Data;

@Data
public class SubjectProfessorDomain {
    private Integer id;
    private Subject subject;
    private User professor;
}
