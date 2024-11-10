package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Activity;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.GroupStudent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityGroupDomain {
    private Integer id;
    private Activity activityDomain;
    private GroupStudent group;
    private LocalDate due;
}

