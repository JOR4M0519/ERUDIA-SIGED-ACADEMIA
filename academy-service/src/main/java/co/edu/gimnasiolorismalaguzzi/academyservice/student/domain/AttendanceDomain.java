package co.edu.gimnasiolorismalaguzzi.academyservice.student.domain;

import co.edu.gimnasiolorismalaguzzi.academyservice.academic.entity.SubjectSchedule;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
public class AttendanceDomain {
    private Integer id;
    private User student;
    private SubjectSchedule schedule;
    private LocalDate attendanceDate;
    private String status;
    private OffsetDateTime recordedAt;
}
