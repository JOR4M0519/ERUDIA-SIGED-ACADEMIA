package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.GroupStudentServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/group-students")
public class GroupStudentController {
    
    private final GroupStudentServicePort groupStudentServicePort;

    public GroupStudentController(GroupStudentServicePort groupStudentServicePort) {
        this.groupStudentServicePort = groupStudentServicePort;
    }
    
    @GetMapping
    public ResponseEntity<List<GroupStudentDomain>> getAllStudentGroups(){
        List<GroupStudentDomain> groupStudentDomains = groupStudentServicePort.getAllGroups();
        return ResponseEntity.ok(groupStudentDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupStudentDomain> getGroupStudentById(@PathVariable Integer id) {
        GroupStudentDomain GroupStudent = groupStudentServicePort.getGroupById(id);
        return ResponseEntity.ok(GroupStudent);
    }

    @PostMapping()
    public ResponseEntity<GroupStudentDomain> createGroupStudent(@RequestBody GroupStudentDomain GroupStudentDomain) {
        GroupStudentDomain createdGroupStudent = groupStudentServicePort.createGroup(GroupStudentDomain);
        return ResponseEntity.ok(createdGroupStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupStudentDomain> updateGroupStudent(@PathVariable Integer id, @RequestBody GroupStudentDomain GroupStudentDomain) {
        GroupStudentDomain updatedGroupStudent = groupStudentServicePort.updateGroup(id, GroupStudentDomain);
        return ResponseEntity.ok(updatedGroupStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupStudent(@PathVariable Integer id) {
        groupStudentServicePort.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
