package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.StudentTrackingServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.StudentTrackingDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/student-tracking")
public class StudentTrackingController {

    private final StudentTrackingServicePort servicePort;
    
    public StudentTrackingController(StudentTrackingServicePort servicePort) {
        this.servicePort = servicePort;
    }
    @GetMapping
    public ResponseEntity<List<StudentTrackingDomain>> getAllStudentTracking(){
        List<StudentTrackingDomain> StudentTrackingDomains = servicePort.getAllTrackings();
        return ResponseEntity.ok(StudentTrackingDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentTrackingDomain> getStudentTrackingById(@PathVariable Integer id) {
        StudentTrackingDomain StudentTracking = servicePort.getTrackingById(id);
        return ResponseEntity.ok(StudentTracking);
    }

    @PostMapping()
    public ResponseEntity<StudentTrackingDomain> createStudentTracking(@RequestBody StudentTrackingDomain StudentTrackingDomain) {
        StudentTrackingDomain createdStudentTracking = servicePort.createTracking(StudentTrackingDomain);
        return ResponseEntity.ok(createdStudentTracking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentTrackingDomain> updateStudentTracking(@PathVariable Integer id, @RequestBody StudentTrackingDomain StudentTrackingDomain) {
        StudentTrackingDomain updatedStudentTracking = servicePort.updateTracking(id, StudentTrackingDomain);
        return ResponseEntity.ok(updatedStudentTracking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentTracking(@PathVariable Integer id) {
        servicePort.deleteTracking(id);
        return ResponseEntity.noContent().build();
    }

}
