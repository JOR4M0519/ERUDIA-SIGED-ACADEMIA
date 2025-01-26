package co.edu.gimnasiolorismalaguzzi.academyservice.administration.controller;

import co.edu.gimnasiolorismalaguzzi.academyservice.administration.service.PersistenceUserDetailPort;
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

    public UserController(PersistenceUserDetailPort persistenceUserDetailPort) {
        this.persistenceUserDetailPort = persistenceUserDetailPort;
    }

    @GetMapping()
    public ResponseEntity<List<UserDetailDomain>> getAllUsers() {
        List<UserDetailDomain> users = persistenceUserDetailPort.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserDetailDomain> getUserById(@PathVariable String uuid) {
        UserDetailDomain user = persistenceUserDetailPort.findById(uuid);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<UserDetailDomain> createUser(@PathVariable String uuid,@RequestBody UserDetailDomain userDetailDomain) {
        UserDetailDomain createdUser = persistenceUserDetailPort.save(userDetailDomain);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable String uuid, @RequestBody UserDetailDomain userDetailDomain) {
        persistenceUserDetailPort.update(uuid, userDetailDomain);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        persistenceUserDetailPort.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
