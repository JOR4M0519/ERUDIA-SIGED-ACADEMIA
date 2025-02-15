package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceAttendancePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/attendance")
public class AttendanceController {
    private final PersistenceAttendancePort port;

    public AttendanceController(PersistenceAttendancePort port) {
        this.port = port;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDomain>> getAllAttendances(){
        List<AttendanceDomain> AttendanceDomainList = port.findAll();
        return ResponseEntity.ok(AttendanceDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDomain> getAttendanceById(@PathVariable Integer id){
        AttendanceDomain attendanceDomain = port.findById(id);
        return ResponseEntity.ok(attendanceDomain);
    }

    @PostMapping
    public ResponseEntity<AttendanceDomain> createAttendance(@RequestBody AttendanceDomain AttendanceDomain){
        AttendanceDomain createdDimension = port.save(AttendanceDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDomain> updateAttendance(@PathVariable Integer id, @RequestBody AttendanceDomain AttendanceDomain){
        AttendanceDomain updatedDimension = port.update(id,AttendanceDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}
