package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.ErrorDto;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceAttendancePort;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/groups/{groupId}/subjects/{subjectId}/professors/{professorId}/periods/{periodId}/batch")
    public ResponseEntity<?> saveAttendances(
            @RequestBody List<AttendanceDomain> attendances,
            @PathVariable Integer groupId,
            @PathVariable Integer subjectId,
            @PathVariable Integer professorId,
            @PathVariable Integer periodId) {

        try {
            List<AttendanceDomain> createdAttendances = attendancePort.saveAll(attendances, groupId, subjectId, professorId, periodId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendances);
        } catch (AppException e) {
            // Si es una AppException, ya tiene el código de estado definido
            return ResponseEntity.status(e.getCode()).body(ErrorDto.builder().message(e.getMessage()).build());
        } catch (IllegalStateException e) {
            // Para excepciones de duplicados (código 409 - Conflict)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorDto.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            // Para otras excepciones no controladas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorDto.builder().message("Error al procesar las asistencias: " + e.getMessage()).build());
        }
    }

    /*@PostMapping("/batch")
    public ResponseEntity<List<AttendanceDomain>> createAttendances(@RequestBody List<AttendanceDomain> attendances) {
        List<AttendanceDomain> createdAttendances = attendancePort.saveAll(attendances);
        return ResponseEntity.ok(createdAttendances);
    }*/

    @PutMapping("/batch")
    public ResponseEntity<List<AttendanceDomain>> updateAttendances(@RequestBody List<AttendanceDomain> attendances) {
       List<AttendanceDomain> updatedAttendances = attendancePort.updateAll(attendances);
        return ResponseEntity.ok(updatedAttendances);
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteAttendances(@RequestBody List<Integer> ids) {
        HttpStatus status = attendancePort.deleteAll(ids);
        return ResponseEntity.status(status).build();
    }


}
