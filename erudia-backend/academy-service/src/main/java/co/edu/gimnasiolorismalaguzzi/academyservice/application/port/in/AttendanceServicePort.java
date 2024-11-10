package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AttendanceDomain;

import java.util.List;

public interface AttendanceServicePort {
    List<AttendanceDomain> getAllAttendances();
    AttendanceDomain getAttendanceById(Integer id);
    AttendanceDomain createAttendance(AttendanceDomain attendanceDomain);
    AttendanceDomain updateAttendance(Integer id, AttendanceDomain attendanceDomain);

}
