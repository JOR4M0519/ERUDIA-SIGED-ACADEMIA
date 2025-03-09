package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.FamilyDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDomain;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceFamilyPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.persistence.PersistenceUserDetailPort;
import co.edu.gimnasiolorismalaguzzi.academyservice.common.WebAdapter;
import co.edu.gimnasiolorismalaguzzi.academyservice.administration.domain.UserDetailDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/academy/users/detail")
public class UserController {

    private final PersistenceUserDetailPort persistenceUserDetailPort;
    private final PersistenceFamilyPort persistenceFamilyPort;

    public UserController(PersistenceUserDetailPort persistenceUserDetailPort, PersistenceFamilyPort persistenceFamilyPort) {
        this.persistenceUserDetailPort = persistenceUserDetailPort;
        this.persistenceFamilyPort = persistenceFamilyPort;
    }

    @GetMapping()
    public ResponseEntity<List<UserDetailDomain>> getAllUsers() {
        List<UserDetailDomain> users = persistenceUserDetailPort.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDomain> getUserById(@PathVariable Integer id) {
        UserDetailDomain user = persistenceUserDetailPort.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<UserDetailDomain> createUser(@RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain createdUser = persistenceUserDetailPort.save(userDetailDomain);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/family/{id}")
    public ResponseEntity<List<FamilyDomain>> findRelatives(@PathVariable Integer id){
        List<FamilyDomain> familyDomain = persistenceFamilyPort.findRelativesByStudent(id);
        return ResponseEntity.ok(familyDomain);
    }

    /**
     * Devuelve una lista de los estudiantes de un familiar
     * @param relativeId
     * @return
     */
    @GetMapping("/family/{relativeId}/students")
    public ResponseEntity<?> findStudentsByRelatives(@PathVariable Integer relativeId){
        List<FamilyDomain> familyDomain = persistenceFamilyPort.findStudentsByRelativeId(relativeId);
        return ResponseEntity.ok(familyDomain);
    }


    @PostMapping("/family/create/{id}")
    public ResponseEntity<FamilyDomain> createRelatives(@PathVariable Integer id, @RequestBody FamilyDomain familyDomain){
        FamilyDomain createdRelative = persistenceFamilyPort.saveById(id,familyDomain);
        return ResponseEntity.ok(createdRelative);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserDetailDomain userDetailDomain) {
        persistenceUserDetailPort.update(id, userDetailDomain);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id ) {
        persistenceUserDetailPort.delete(id);
        return ResponseEntity.noContent().build();
    }

}
