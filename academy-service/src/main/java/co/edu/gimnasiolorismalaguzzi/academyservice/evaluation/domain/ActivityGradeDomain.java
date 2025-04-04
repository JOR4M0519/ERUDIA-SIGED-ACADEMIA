package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivityGradeDomain {
    private Integer id;
    private UserDomain student;
    private ActivityGroupDomain activity;
    private BigDecimal score;
    private String comment;
}
