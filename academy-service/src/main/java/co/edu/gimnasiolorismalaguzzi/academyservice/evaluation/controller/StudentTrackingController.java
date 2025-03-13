package co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.service.persistence.PersistenceStudentTrackingPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.evaluation.domain.StudentTrackingDomain;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/student-tracking")
public class StudentTrackingController {

    private final PersistenceStudentTrackingPort persistenceStudentTrackingPort;
    
    public StudentTrackingController(PersistenceStudentTrackingPort persistenceStudentTrackingPort) {
        this.persistenceStudentTrackingPort = persistenceStudentTrackingPort;
    }
    @GetMapping
    public ResponseEntity<List<StudentTrackingDomain>> getAllStudentTracking(){
        List<StudentTrackingDomain> StudentTrackingDomains = persistenceStudentTrackingPort.findAll();
        return ResponseEntity.ok(StudentTrackingDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentTrackingDomain> getStudentTrackingById(@PathVariable Integer id) {
        StudentTrackingDomain StudentTracking = persistenceStudentTrackingPort.findById(id);
        return ResponseEntity.ok(StudentTracking);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<List<?>> getTrackingByStudentId(@PathVariable Integer id){
        List<StudentTrackingDomain> StudentTrackingDomains = persistenceStudentTrackingPort.getTrackingByStudentId(id);
        return ResponseEntity.ok(StudentTrackingDomains);
    }

    @GetMapping("/students/final-observations/{id1}/period/{id2}")
    public ResponseEntity<?> getFinalObservationByStudent(@PathVariable Integer id1, @PathVariable Integer id2){
        StudentTrackingDomain studentTrackingDomain = persistenceStudentTrackingPort.getFinalObservationByStudentId(id1,id2);
        return ResponseEntity.ok(studentTrackingDomain);
    }

    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<?> getTrackingListStudentsByTeacherId(@PathVariable Integer teacherId){
        String status = "I";
        List<StudentTrackingDomain> StudentTrackingDomains = persistenceStudentTrackingPort.getTrackingListStudentsCreatedByteacher(teacherId,status);
        return ResponseEntity.ok(StudentTrackingDomains);
    }

    @PostMapping()
    public ResponseEntity<StudentTrackingDomain> createStudentTracking(@RequestBody StudentTrackingDomain StudentTrackingDomain) {
        StudentTrackingDomain createdStudentTracking = persistenceStudentTrackingPort.save(StudentTrackingDomain);
        return ResponseEntity.ok(createdStudentTracking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentTrackingDomain> updateStudentTracking(@PathVariable Integer id, @RequestBody StudentTrackingDomain StudentTrackingDomain) {
        StudentTrackingDomain updatedStudentTracking = persistenceStudentTrackingPort.update(id, StudentTrackingDomain);
        return ResponseEntity.ok(updatedStudentTracking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentTracking(@PathVariable Integer id) {
        persistenceStudentTrackingPort.delete(id);
        return ResponseEntity.noContent().build();
    }

}
