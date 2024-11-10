package co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in;

import co.edu.gimnasiolorismalaguzzi.academyservice.domain.StudentTrackingDomain;

import java.util.List;

public interface StudentTrackingServicePort {
    List<StudentTrackingDomain> getAllTrackings();
    StudentTrackingDomain getTrackingById(Integer id);
    StudentTrackingDomain createTracking(StudentTrackingDomain studentTrackingDomain);
    StudentTrackingDomain updateTracking(Integer id, StudentTrackingDomain studentTrackingDomain);
    void deleteTracking(Integer id);
}
