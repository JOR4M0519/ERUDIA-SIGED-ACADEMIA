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
    private final PersistenceAttendancePort attendancePort;

    public AttendanceController(PersistenceAttendancePort attendancePort) {
        this.attendancePort = attendancePort;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDomain>> getAllAttendances(){
        List<AttendanceDomain> AttendanceDomainList = attendancePort.findAll();
        return ResponseEntity.ok(AttendanceDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDomain> getAttendanceById(@PathVariable Integer id){
        AttendanceDomain attendanceDomain = attendancePort.findById(id);
        return ResponseEntity.ok(attendanceDomain);
    }

    @GetMapping("/groups/{groupId}/subjects/{subjectId}/periods/{periodId}/users")
    public ResponseEntity<List<AttendanceDomain>> getHistoricalAttendance(@PathVariable Integer groupId, @PathVariable Integer subjectId, @PathVariable Integer periodId){
        List<AttendanceDomain> attendanceDomain = attendancePort.getHistoricalAttendance(groupId,subjectId,periodId);
        return ResponseEntity.ok(attendanceDomain);
    }

    @PostMapping
    public ResponseEntity<AttendanceDomain> createAttendance(@RequestBody AttendanceDomain AttendanceDomain){
        AttendanceDomain createdDimension = attendancePort.save(AttendanceDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDomain> updateAttendance(@PathVariable Integer id, @RequestBody AttendanceDomain AttendanceDomain){
        AttendanceDomain updatedDimension = attendancePort.update(id,AttendanceDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}
