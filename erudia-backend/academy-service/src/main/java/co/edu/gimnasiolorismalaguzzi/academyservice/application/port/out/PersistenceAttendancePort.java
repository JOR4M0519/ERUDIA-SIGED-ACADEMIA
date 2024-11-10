package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.out.persistence.entity.Attendance;

public interface PersistenceAttendancePort extends PersistencePort<AttendanceDomain, Integer>{
}
