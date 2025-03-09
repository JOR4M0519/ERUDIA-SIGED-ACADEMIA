package co.edu.gimnasiolorismalaguzzi.academyservice.student.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.domain.GroupStudentsDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.student.service.persistence.PersistenceGroupStudentPort;
import jakarta.ws.rs.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/student-groups")
public class GroupStudentsController {
    private final PersistenceGroupStudentPort groupStudentPort;

    public GroupStudentsController(PersistenceGroupStudentPort groupStudentPort) {
        this.groupStudentPort = groupStudentPort;
    }

    @GetMapping
    public ResponseEntity<List<GroupStudentsDomain>> getAllStudentsInGroups(){
        List<GroupStudentsDomain> groupStudentsDomains = groupStudentPort.findAll();
        return ResponseEntity.ok(groupStudentsDomains);
    }

    @GetMapping("/active")
    public ResponseEntity<List<GroupStudentsDomain>> getAllStudentsInGroupsByStatus(){
        String status = "A";
        List<GroupStudentsDomain> groupStudentsDomains = groupStudentPort.getGroupListByStatus(status);
        return ResponseEntity.ok(groupStudentsDomains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupStudentsDomain> getGroupStudentById(@PathVariable Integer id){
        GroupStudentsDomain groupStudentsDomain = groupStudentPort.findById(id);
        return ResponseEntity.ok(groupStudentsDomain);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getGroupsStudentByUsername(@PathVariable Integer id){
        List<GroupStudentsDomain> groupStudentsList = groupStudentPort.getGroupsStudentById(id,"A");
        return ResponseEntity.ok(groupStudentsList);
    }
    @GetMapping("/groups/{groupId}/users")
    public ResponseEntity<?> getGroupsStudentByGroupId(@PathVariable Integer groupId){
        List<GroupStudentsDomain> groupStudentsList = groupStudentPort.getGroupsStudentByGroupId(groupId,"I");
        return ResponseEntity.ok(groupStudentsList);
    }

    /**
     * Trae la lista de estudiantes del grupo de un profesor
     * @param mentorId
     * @return
     */
    @GetMapping("/mentors/{mentorId}/students") //?year={year}
    public ResponseEntity<?> getListByMentorIdByYear(@PathVariable Integer mentorId
                                              // ,@RequestParam("year") Integer year
    ){
        List<GroupStudentsDomain> groupStudentsList = groupStudentPort.getListByMentorIdByYear(mentorId,2025);
        return ResponseEntity.ok(groupStudentsList);
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
