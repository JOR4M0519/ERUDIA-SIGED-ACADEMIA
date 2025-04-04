package co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.PersistencePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface PersistenceAttendancePort extends PersistencePort<AttendanceDomain, Integer> {
    List<AttendanceDomain> getHistoricalAttendance(Integer groupId, Integer subjectId, Integer periodId);

    @Transactional
    List<AttendanceDomain> saveAll(List<AttendanceDomain> attendances,
                                   Integer groupId,
                                   Integer subjectId,
                                   Integer professorId,
                                   Integer periodId);

    List<AttendanceDomain> updateAll(List<AttendanceDomain> attendances);
    HttpStatus deleteAll(List<Integer> ids);
}
