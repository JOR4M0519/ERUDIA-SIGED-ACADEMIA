package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;

import java.util.List;

public interface PersistenceAttendancePort extends PersistencePort<AttendanceDomain, Integer> {
    List<AttendanceDomain> getHistoricalAttendance(Integer groupId, Integer subjectId, Integer periodId);
}
