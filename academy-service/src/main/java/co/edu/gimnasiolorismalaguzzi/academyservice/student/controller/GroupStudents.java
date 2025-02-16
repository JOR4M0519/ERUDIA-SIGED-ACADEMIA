package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.entity.GroupStudent;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.ws.rs.core.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/student-groups")
public class GroupStudents {
    private final PersistenceGroupStudentPort groupStudentPort;

    public GroupStudents(PersistenceGroupStudentPort groupStudentPort) {
        this.groupStudentPort = groupStudentPort;
    }

    @GetMapping
    public ResponseEntity<List<GroupStudentsDomain>> getAllStudentsInGroups(){
        List<GroupStudentsDomain> groupStudentsDomains = groupStudentPort.findAll();
        return ResponseEntity.ok(groupStudentsDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupStudentsDomain> getGroupStudentById(@PathVariable Integer id){
        GroupStudentsDomain groupStudentsDomain = groupStudentPort.findById(id);
        return ResponseEntity.ok(groupStudentsDomain);
    }

    @PostMapping()
    public ResponseEntity<GroupStudentsDomain> createGroupStudent(@RequestBody GroupStudentsDomain groupStudentsDomain){
        GroupStudentsDomain groupStudentsDomain1 = groupStudentPort.save(groupStudentsDomain);
        return ResponseEntity.ok(groupStudentsDomain1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupStudentsDomain> updateGroupStudent(@PathVariable Integer id, @RequestBody GroupStudentsDomain groupStudentsDomain){
        GroupStudentsDomain updated = groupStudentPort.update(id,groupStudentsDomain);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupStudents(@PathVariable Integer id){
        groupStudentPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
