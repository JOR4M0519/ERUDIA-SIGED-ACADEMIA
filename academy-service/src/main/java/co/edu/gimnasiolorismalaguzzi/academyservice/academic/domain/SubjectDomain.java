package co.edu.gimnasiolorismalaguzzi.academyservice.academic.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class SubjectDomain {
    private Integer id;
    private String subjectName;
    private String status;
    private List<User> professor; //Campo externo
}
