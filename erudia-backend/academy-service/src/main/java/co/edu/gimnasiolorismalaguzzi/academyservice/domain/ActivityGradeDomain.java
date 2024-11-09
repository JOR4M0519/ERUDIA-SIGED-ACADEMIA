package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.ActivityGroup;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivityGradeDomain {
    private Integer id;
    private User student;
    private ActivityGroup activity;
    private BigDecimal score;
    private String comment;
}
