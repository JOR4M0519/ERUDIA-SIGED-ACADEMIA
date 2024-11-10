package co.edu.gimnasiolorismalaguzzi.academyservice.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class AttendanceDomain {
    private Integer id;
    private User student;
    private SubjectSchedule schedule;
    private LocalDate attendanceDate;
    private String status;
    private OffsetDateTime recordedAt;
}
