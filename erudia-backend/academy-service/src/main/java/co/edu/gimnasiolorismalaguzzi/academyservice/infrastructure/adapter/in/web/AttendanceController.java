package co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.adapter.in.web;

import co.edu.gimnasiolorismalaguzzi.academyservice.application.port.in.AttendanceServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.domain.AttendanceDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/attendance")
public class AttendanceController {
    private final AttendanceServicePort port;

    public AttendanceController(AttendanceServicePort port) {
        this.port = port;
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDomain>> getAllAttendances(){
        List<AttendanceDomain> AttendanceDomainList = port.getAllAttendances();
        return ResponseEntity.ok(AttendanceDomainList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDomain> getAttendanceById(@PathVariable Integer id){
        AttendanceDomain attendanceDomain = port.getAttendanceById(id);
        return ResponseEntity.ok(attendanceDomain);
    }

    @PostMapping
    public ResponseEntity<AttendanceDomain> createAttendance(@RequestBody AttendanceDomain AttendanceDomain){
        AttendanceDomain createdDimension = port.createAttendance(AttendanceDomain);
        return ResponseEntity.ok(createdDimension);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDomain> updateAttendance(@PathVariable Integer id, @RequestBody AttendanceDomain AttendanceDomain){
        AttendanceDomain updatedDimension = port.updateAttendance(id,AttendanceDomain);
        return ResponseEntity.ok(updatedDimension);
    }
}
