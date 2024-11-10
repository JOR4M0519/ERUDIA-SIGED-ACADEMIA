package co.edu.gimnasiolorismalaguzzi.academyservice.application.service;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.StudentTrackingServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.out.PersistenceStudentTrackingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.UseCase;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.StudentTrackingDomain;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class StudentTrackingServiceImpl implements StudentTrackingServicePort {

    private final PersistenceStudentTrackingPort port;

    @Autowired
    public StudentTrackingServiceImpl(PersistenceStudentTrackingPort port) {
        this.port = port;
    }


    @Override
    public List<StudentTrackingDomain> getAllTrackings() {
        return port.findAll();
    }

    @Override
    public StudentTrackingDomain getTrackingById(Integer id) {
        return port.findById(id);
    }

    @Override
    public StudentTrackingDomain createTracking(StudentTrackingDomain studentTrackingDomain) {
        return port.save(studentTrackingDomain);
    }

    @Override
    public StudentTrackingDomain updateTracking(Integer id, StudentTrackingDomain studentTrackingDomain) {
        return port.update(id, studentTrackingDomain);
    }

    @Override
    public void deleteTracking(Integer id) {
        port.delete(id);
    }
}
