package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.AttendanceServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceAttendancePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AttendanceDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class AttendanceServiceImpl implements AttendanceServicePort {

    private final PersistenceAttendancePort port;

    @Autowired
    public AttendanceServiceImpl(PersistenceAttendancePort persistenceAttendancePort){
        this.port = persistenceAttendancePort;
    }

    @Override
    public List<AttendanceDomain> getAllAttendances() {
        return port.findAll();
    }

    @Override
    public AttendanceDomain getAttendanceById(Integer id) {
        return port.findById(id);
    }

    @Override
    public AttendanceDomain createAttendance(AttendanceDomain attendanceDomain) {
        return port.save(attendanceDomain);
    }

    @Override
    public AttendanceDomain updateAttendance(Integer id, AttendanceDomain attendanceDomain) {
        return port.update(id,attendanceDomain);
    }
}
