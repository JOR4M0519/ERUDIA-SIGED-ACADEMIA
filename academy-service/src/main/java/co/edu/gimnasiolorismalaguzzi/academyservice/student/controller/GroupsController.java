package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.infrastructure.exception.AppException;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.AttendanceReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.RepeatingStudentsGroupReportDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.ReportGroupsStatusDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupsPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/groups")
public class GroupsController {
    
    private final PersistenceGroupsPort persistenceGroupsPort;

    public GroupsController(PersistenceGroupsPort persistenceGroupsPort) {
        this.persistenceGroupsPort = persistenceGroupsPort;
    }
    
    @GetMapping
    public ResponseEntity<List<GroupsDomain>> getAllStudentGroups(){
        List<GroupsDomain> groupsDomains = persistenceGroupsPort.findAll();
        return ResponseEntity.ok(groupsDomains);
    }

    @GetMapping("/active")
    public ResponseEntity<List<GroupsDomain>> getAllStudentGroupsByStatus(){
        List<GroupsDomain> groupsDomains = persistenceGroupsPort.findByStatus("A");
        return ResponseEntity.ok(groupsDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupsDomain> getGroupStudentById(@PathVariable Integer id) {
        GroupsDomain GroupStudent = persistenceGroupsPort.findById(id);
        return ResponseEntity.ok(GroupStudent);
    }

    @PostMapping()
    public ResponseEntity<GroupsDomain> createGroupStudent(@RequestBody GroupsDomain GroupsDomain) {
        GroupsDomain createdGroupStudent = persistenceGroupsPort.save(GroupsDomain);
        return ResponseEntity.ok(createdGroupStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupsDomain> updateGroupStudent(@PathVariable Integer id, @RequestBody GroupsDomain GroupsDomain) {
        GroupsDomain updatedGroupStudent = persistenceGroupsPort.update(id, GroupsDomain);
        return ResponseEntity.ok(updatedGroupStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroupStudent(@PathVariable Integer id) {
        try {
            persistenceGroupsPort.delete(id);
            return ResponseEntity.noContent().build();
        } catch (AppException e) {
            if (e.getCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(e.getMessage());
            } else if (e.getCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la solicitud");
        }
    }

    @GetMapping("/students/attendance")
    public ResponseEntity<List<AttendanceReportDomain>> getAttendanceReport() {
        List<AttendanceReportDomain> report = persistenceGroupsPort.getAttendanceReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/students/status")
    public ResponseEntity<List<ReportGroupsStatusDomain>> getAcademicLevelReport() {
        return new ResponseEntity<>(persistenceGroupsPort.getAcademicLevelReport(), HttpStatus.OK);
    }

    @GetMapping("/students/repeating")
    public ResponseEntity<List<RepeatingStudentsGroupReportDomain>> getRepeatingStudentsByGroupReport() {
        List<RepeatingStudentsGroupReportDomain> report = persistenceGroupsPort.getRepeatingStudentsByGroupReport();
        return ResponseEntity.ok(report);
    }

}
