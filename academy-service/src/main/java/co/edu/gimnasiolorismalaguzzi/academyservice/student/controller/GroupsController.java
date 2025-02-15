package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.GroupServicePort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/groups")
public class GroupsController {
    
    private final GroupServicePort groupServicePort;

    public GroupsController(GroupServicePort groupServicePort) {
        this.groupServicePort = groupServicePort;
    }
    
    @GetMapping
    public ResponseEntity<List<GroupsDomain>> getAllStudentGroups(){
        List<GroupsDomain> groupsDomains = groupServicePort.getAllGroups();
        return ResponseEntity.ok(groupsDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupsDomain> getGroupStudentById(@PathVariable Integer id) {
        GroupsDomain GroupStudent = groupServicePort.getGroupById(id);
        return ResponseEntity.ok(GroupStudent);
    }

    @PostMapping()
    public ResponseEntity<GroupsDomain> createGroupStudent(@RequestBody GroupsDomain GroupsDomain) {
        GroupsDomain createdGroupStudent = groupServicePort.createGroup(GroupsDomain);
        return ResponseEntity.ok(createdGroupStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupsDomain> updateGroupStudent(@PathVariable Integer id, @RequestBody GroupsDomain GroupsDomain) {
        GroupsDomain updatedGroupStudent = groupServicePort.updateGroup(id, GroupsDomain);
        return ResponseEntity.ok(updatedGroupStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupStudent(@PathVariable Integer id) {
        groupServicePort.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
